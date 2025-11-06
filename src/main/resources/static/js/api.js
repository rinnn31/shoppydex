const BASE_URL = '/api';

async function performRequest(endpoint, method = 'GET', params = null, data = null, authRequired = true, preferAuthToken = false) {
    let url = `${BASE_URL}${endpoint}`;
    let token = null;
    if (authRequired || preferAuthToken) {
        token = localStorage.getItem('authToken') || null;
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
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || 'An error occurred');
    }
    return response.json();
};

export const AuthenticationAPI = {
    login : (credentials) => performRequest('/auth/login', 'POST', null, credentials, false),
    register : (userInfo) => performRequest('/auth/register', 'POST', null, userInfo, false),
    sendResetPassword : (user) => performRequest('/auth/send-mail-reset-password', 'GET', { username : user }, null ,false),
    sendVerifyMail: (user) => performRequest('/auth/send-mail-verification', 'GET', { username : user }, null ,true),
    resetPassword: (data) => performRequest('/auth/reset-password', 'POST', null, data, false)
};

export const UserAPI = {
    getProfile : () => performRequest('/user/info', 'GET', null, true),
    changePassword: (data) => performRequest('/user/change-password', 'POST', null, passwords, true),
    
}

const ProductAPI = {
    getAllCategories: () => performRequest('/products/categories', 'GET', null, false, true),
    getAllProducts: (categoryId) => {
        const params = categoryId ? { categoryId } : null;
        return performRequest('/products', 'GET', params, null, false, true);
    },
    addProduct : (productData) => performRequest('/products', 'POST', null, productData, true),
    deleteProduct : (productId) => performRequest('/products', 'DELETE', { productId }, null, true),
    updateProduct : (productData) => performRequest('/products', 'PATCH', productData, true),

    getProductItems : (productId) => performRequest('/product/items', 'GET', {productId}, null, false, true),
    getProductItemById : (itemId) => performRequest(`/product/items/${itemId}`, 'GET', null, null, false, true),
    addProductItem : (itemData) => performRequest('/product/items', 'POST', null, itemData, true),
    updateProductItem : (itemData) => performRequest('/product/items', 'PATCH', null, itemData, true),
    deleteProductItem : (itemId) => performRequest(`/product/items/${itemId}`, 'DELETE',null, null, true),
    deleteAllProductItemsByProductId : (productId) => performRequest('/product/items', 'DELETE', {productId}, null, true),
}