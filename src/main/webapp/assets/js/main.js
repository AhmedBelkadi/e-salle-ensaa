// Main JavaScript for E-Salle ENSAA

document.addEventListener('DOMContentLoaded', function() {
    // Initialize tooltips
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });

    // Initialize popovers
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl);
    });

    // Auto-hide alerts after 5 seconds
    var alerts = document.querySelectorAll('.alert:not(.alert-permanent)');
    alerts.forEach(function(alert) {
        setTimeout(function() {
            if (alert && alert.parentNode) {
                var bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            }
        }, 5000);
    });

    // Form validation
    var forms = document.querySelectorAll('.needs-validation');
    Array.prototype.slice.call(forms).forEach(function(form) {
        form.addEventListener('submit', function(event) {
            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        }, false);
    });

    // Search functionality
    var searchInput = document.getElementById('search');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(function() {
            var searchTerm = this.value.trim();
            if (searchTerm.length >= 2) {
                performSearch(searchTerm);
            } else if (searchTerm.length === 0) {
                clearSearch();
            }
        }, 300));
    }

    // Filter functionality
    var filterButtons = document.querySelectorAll('[data-filter]');
    filterButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var filter = this.getAttribute('data-filter');
            applyFilter(filter);
        });
    });

    // Toggle availability
    var toggleButtons = document.querySelectorAll('[data-toggle-availability]');
    toggleButtons.forEach(function(button) {
        button.addEventListener('click', function() {
            var salleId = this.getAttribute('data-salle-id');
            toggleSalleAvailability(salleId);
        });
    });
});

// Utility Functions
function debounce(func, wait) {
    var timeout;
    return function executedFunction() {
        var later = function() {
            clearTimeout(timeout);
            func.apply(this, arguments);
        }.bind(this);
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Search Functions
function performSearch(searchTerm) {
    var url = new URL(window.location);
    url.searchParams.set('q', searchTerm);
    url.searchParams.delete('page');
    window.location.href = url.toString();
}

function clearSearch() {
    var url = new URL(window.location);
    url.searchParams.delete('q');
    url.searchParams.delete('page');
    window.location.href = url.toString();
}

// Filter Functions
function applyFilter(filter) {
    var url = new URL(window.location);
    url.searchParams.set('action', filter);
    url.searchParams.delete('page');
    window.location.href = url.toString();
}

// AJAX Functions
function toggleSalleAvailability(salleId) {
    showLoading();
    
    fetch('/salles/' + salleId + '/toggle', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-Requested-With': 'XMLHttpRequest'
        }
    })
    .then(response => response.json())
    .then(data => {
        hideLoading();
        if (data.success) {
            showNotification('Statut mis à jour avec succès', 'success');
            location.reload();
        } else {
            showNotification('Erreur: ' + data.message, 'error');
        }
    })
    .catch(error => {
        hideLoading();
        console.error('Error:', error);
        showNotification('Erreur lors de la mise à jour', 'error');
    });
}

function deleteSalle(salleId, salleName) {
    if (confirm('Êtes-vous sûr de vouloir supprimer la salle "' + salleName + '" ?\n\nCette action est irréversible.')) {
        showLoading();
        
        fetch('/salles/' + salleId, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'X-Requested-With': 'XMLHttpRequest'
            }
        })
        .then(response => response.json())
        .then(data => {
            hideLoading();
            if (data.success) {
                showNotification('Salle supprimée avec succès', 'success');
                setTimeout(function() {
                    window.location.href = '/salles';
                }, 1000);
            } else {
                showNotification('Erreur: ' + data.message, 'error');
            }
        })
        .catch(error => {
            hideLoading();
            console.error('Error:', error);
            showNotification('Erreur lors de la suppression', 'error');
        });
    }
}

// UI Helper Functions
function showLoading() {
    var loadingOverlay = document.createElement('div');
    loadingOverlay.id = 'loading-overlay';
    loadingOverlay.className = 'position-fixed top-0 start-0 w-100 h-100 d-flex justify-content-center align-items-center';
    loadingOverlay.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
    loadingOverlay.style.zIndex = '9999';
    loadingOverlay.innerHTML = '<div class="spinner-border text-light" role="status"><span class="visually-hidden">Chargement...</span></div>';
    document.body.appendChild(loadingOverlay);
}

function hideLoading() {
    var loadingOverlay = document.getElementById('loading-overlay');
    if (loadingOverlay) {
        loadingOverlay.remove();
    }
}

function showNotification(message, type) {
    var alertClass = type === 'success' ? 'alert-success' : 'alert-danger';
    var icon = type === 'success' ? 'bi-check-circle' : 'bi-exclamation-triangle';
    
    var notification = document.createElement('div');
    notification.className = 'alert ' + alertClass + ' alert-dismissible fade show position-fixed';
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.zIndex = '9999';
    notification.style.minWidth = '300px';
    notification.innerHTML = '<i class="bi ' + icon + '"></i> ' + message + 
                           '<button type="button" class="btn-close" data-bs-dismiss="alert"></button>';
    
    document.body.appendChild(notification);
    
    // Auto-remove after 5 seconds
    setTimeout(function() {
        if (notification && notification.parentNode) {
            var bsAlert = new bootstrap.Alert(notification);
            bsAlert.close();
        }
    }, 5000);
}

// Form Enhancement Functions
function enhanceForm(formId) {
    var form = document.getElementById(formId);
    if (!form) return;
    
    // Add real-time validation
    var inputs = form.querySelectorAll('input, select, textarea');
    inputs.forEach(function(input) {
        input.addEventListener('blur', function() {
            validateField(this);
        });
        
        input.addEventListener('input', function() {
            clearFieldError(this);
        });
    });
}

function validateField(field) {
    var isValid = field.checkValidity();
    var feedback = field.parentNode.querySelector('.invalid-feedback');
    
    if (!isValid) {
        field.classList.add('is-invalid');
        if (feedback) {
            feedback.textContent = field.validationMessage;
        }
    } else {
        field.classList.remove('is-invalid');
        field.classList.add('is-valid');
    }
    
    return isValid;
}

function clearFieldError(field) {
    field.classList.remove('is-invalid', 'is-valid');
}

// Data Table Enhancement
function enhanceDataTable(tableId) {
    var table = document.getElementById(tableId);
    if (!table) return;
    
    // Add sorting functionality
    var headers = table.querySelectorAll('th[data-sort]');
    headers.forEach(function(header) {
        header.style.cursor = 'pointer';
        header.addEventListener('click', function() {
            sortTable(table, this.getAttribute('data-sort'));
        });
    });
}

function sortTable(table, column) {
    var tbody = table.querySelector('tbody');
    var rows = Array.from(tbody.querySelectorAll('tr'));
    var isAscending = table.getAttribute('data-sort-direction') !== 'asc';
    
    rows.sort(function(a, b) {
        var aValue = a.querySelector('td[data-sort="' + column + '"]').textContent.trim();
        var bValue = b.querySelector('td[data-sort="' + column + '"]').textContent.trim();
        
        if (isAscending) {
            return aValue.localeCompare(bValue);
        } else {
            return bValue.localeCompare(aValue);
        }
    });
    
    rows.forEach(function(row) {
        tbody.appendChild(row);
    });
    
    table.setAttribute('data-sort-direction', isAscending ? 'asc' : 'desc');
}

// Export Functions
function exportToCSV() {
    var table = document.querySelector('table');
    if (!table) return;
    
    var csv = [];
    var rows = table.querySelectorAll('tr');
    
    rows.forEach(function(row) {
        var cells = row.querySelectorAll('td, th');
        var rowData = [];
        cells.forEach(function(cell) {
            rowData.push('"' + cell.textContent.replace(/"/g, '""') + '"');
        });
        csv.push(rowData.join(','));
    });
    
    var csvContent = csv.join('\n');
    var blob = new Blob([csvContent], { type: 'text/csv' });
    var url = window.URL.createObjectURL(blob);
    var a = document.createElement('a');
    a.href = url;
    a.download = 'salles.csv';
    a.click();
    window.URL.revokeObjectURL(url);
}

// Print Functions
function printPage() {
    window.print();
}

// Keyboard Shortcuts
document.addEventListener('keydown', function(e) {
    // Ctrl + N: New salle
    if (e.ctrlKey && e.key === 'n') {
        e.preventDefault();
        window.location.href = '/salles?action=add';
    }
    
    // Ctrl + F: Focus search
    if (e.ctrlKey && e.key === 'f') {
        e.preventDefault();
        var searchInput = document.getElementById('search');
        if (searchInput) {
            searchInput.focus();
        }
    }
    
    // Escape: Close modals
    if (e.key === 'Escape') {
        var modals = document.querySelectorAll('.modal.show');
        modals.forEach(function(modal) {
            var bsModal = bootstrap.Modal.getInstance(modal);
            if (bsModal) {
                bsModal.hide();
            }
        });
    }
});
