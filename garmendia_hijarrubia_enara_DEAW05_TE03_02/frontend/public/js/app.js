const API_URL = 'http://localhost:8080/api/products';

document.addEventListener('DOMContentLoaded', () => {
    loadProducts();
    document.getElementById('productForm').addEventListener('submit', handleFormSubmit);
});

async function loadProducts() {
    try {
        const response = await fetch(API_URL);
        const products = await response.json();
        renderProducts(products);
    } catch (error) {
        console.error('Error loading products:', error);
    }
}

function renderProducts(products) {
    const tbody = document.querySelector('#productsTable tbody');
    tbody.innerHTML = '';
    
    if (products.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5" class="empty-table">No products found. Add your first product!</td></tr>';
        return;
    }
    
    products.forEach(product => {
        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${product.id}</td>
            <td>${product.name}</td>
            <td>$${product.price.toFixed(2)}</td>
            <td>${product.description || '-'}</td>
            <td>
                <div class="action-buttons">
                    <button onclick="editProduct(${product.id})" class="btn btn-success">Edit</button>
                    <button onclick="deleteProduct(${product.id})" class="btn btn-danger">Delete</button>
                </div>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

async function handleFormSubmit(e) {
    e.preventDefault();
    
    const product = {
        name: document.getElementById('name').value,
        price: parseFloat(document.getElementById('price').value),
        description: document.getElementById('description').value
    };
    
    const productId = document.getElementById('productId').value;
    const method = productId ? 'PUT' : 'POST';
    const url = productId ? `${API_URL}/${productId}` : API_URL;
    
    try {
        const response = await fetch(url, {
            method,
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(product)
        });
        
        if (response.ok) {
            clearForm();
            loadProducts();
        }
    } catch (error) {
        console.error('Error saving product:', error);
    }
}

async function editProduct(id) {
    try {
        const response = await fetch(`${API_URL}/${id}`);
        const product = await response.json();
        
        document.getElementById('productId').value = product.id;
        document.getElementById('name').value = product.name;
        document.getElementById('price').value = product.price;
        document.getElementById('description').value = product.description || '';
    } catch (error) {
        console.error('Error editing product:', error);
    }
}

async function deleteProduct(id) {
    if (confirm('Are you sure you want to delete this product?')) {
        try {
            const response = await fetch(`${API_URL}/${id}`, {
                method: 'DELETE'
            });
            
            if (response.ok) {
                loadProducts();
            }
        } catch (error) {
            console.error('Error deleting product:', error);
        }
    }
}

function clearForm() {
    document.getElementById('productForm').reset();
    document.getElementById('productId').value = '';
}