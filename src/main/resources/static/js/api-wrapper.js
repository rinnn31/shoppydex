const BASE_URL = '/api';

async function performRequest(endpoint, method = 'GET', params = null, data = null, authRequired = true, preferAuthToken = false) {
    let url = `${BASE_URL}${endpoint}`;
    let token = null;
    if (authRequired || preferAuthToken) {
        token = localStorage.getItem('authToken') || null;
        if (token === undefined) {
            token = null;
        }
    }
    
    if (params) {
        const queryParams = new URLSearchParams(params);
        url += `?${queryParams.toString()}`;
    }
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json',
            ...(token ? { 'Authorization': `Bearer ${token}` } : {}),
        },
        ...(data ? { body: JSON.stringify(data) } : {}),
    };
    
    const response = await fetch(url, options);
    try {
        return await response.json();
    } catch (e) {
        return null;
    }
};

const AuthenticationAPI = {
    login : (credentials) => performRequest('/auth/login', 'POST', null, credentials, false),
    register : (userInfo) => performRequest('/auth/register', 'POST', null, userInfo, false),
    sendResetPassword : (user) => performRequest('/auth/send-reset-code', 'GET', { username : user }, null ,false),
    resetPassword: (data) => performRequest('/auth/reset-password', 'POST', null, data, false)
};

const UserAPI = {
    getProfile : () => performRequest('/user/info', 'GET', null, null,  true),
    changePassword: (data) => performRequest('/user/change-password', 'POST', null, data, true),
    changeEmail: (data) => performRequest('/user/change-email', 'POST', null, data, true),
}

const ProductAPI = {
    getAllCategories: () => performRequest('/products/categories', 'GET', null, false, true),
    getAllProducts: (categories, page, size) => {
        const params = {};
        if (categories && categories.length > 0) {
            params.categories = categories.join(',');
        }
        if (page !== undefined) {
            params.page = page;
        }
        if (size !== undefined) {
            params.size = size;
        }
        
        return performRequest('/products', 'GET', params, null, false, true);
    },
    getProduct: (productId) => performRequest(`/products/${productId}`, 'GET', null, null, false, true),
    addProduct : (productData) => performRequest('/products', 'POST', null, productData, true),
    deleteProduct : (productId) => performRequest(`/products/${productId}`, 'DELETE', null, null, true),
    updateProduct : (productData) => performRequest('/products', 'PATCH', null, productData, true),

    addItemsToProduct(productId, items) {
        return performRequest(`/products/${productId}/items`, 'POST', null, items, true);
    },
    clearItemsFromProduct(productId) {
        return performRequest(`/products/${productId}/items`, 'DELETE', null, null, true);
    }

};

const OrderAPI = {
    placeOrder: (orderData) => performRequest('/orders/buy', 'POST', null, orderData, true),
    getUserOrders: () => performRequest('/orders', 'GET', null, null, true),
};