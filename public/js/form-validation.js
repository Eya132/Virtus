document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form[name="user"]');
    const isEditForm = form.dataset.isEdit === 'true';

    // Configuration des règles de validation
    const validationRules = {
        'user_email_user': {
            pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            message: "Veuillez entrer une adresse email valide",
            required: true
        },
        'user_telephone_user': {
            pattern: /^[259]\d{7,}$/,
            message: "Doit commencer par 2, 5 ou 9 et contenir 8 chiffres",
            required: true
        },
        'user_salaire': {
            validate: function(value) {
                return value === '' || parseFloat(value) >= 0;
            },
            message: "Le salaire ne peut pas être négatif",
            required: false
        },
        'user_maxDistance_user': {
            validate: function(value) {
                return value === '' || parseInt(value) >= 0;
            },
            message: "La distance ne peut pas être négative",
            required: false
        }
    };

    // Applique la validation à tous les champs
    Object.keys(validationRules).forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('blur', function() {
                validateField(this, validationRules[fieldId]);
            });
            
            if (!isEditForm || fieldId !== 'user_password_user') {
                field.addEventListener('input', function() {
                    validateField(this, validationRules[fieldId]);
                });
            }
        }
    });

    function validateField(field, rules) {
        const value = field.value.trim();
        let isValid = true;
        let message = '';
        
        if (rules.required && value === '') {
            isValid = false;
            message = 'Ce champ est obligatoire';
        } else if (value !== '') {
            if (rules.pattern && !rules.pattern.test(value)) {
                isValid = false;
                message = rules.message;
            } else if (rules.validate && !rules.validate(value)) {
                isValid = false;
                message = rules.message;
            }
        }
        
        updateFieldValidation(field, isValid, message);
        return isValid;
    }

    function updateFieldValidation(field, isValid, message) {
        const feedbackId = `${field.id}_feedback`;
        let feedbackElement = document.getElementById(feedbackId);
        
        if (!isValid) {
            field.classList.add('is-invalid');
            field.classList.remove('is-valid');
            
            if (!feedbackElement) {
                feedbackElement = document.createElement('div');
                feedbackElement.id = feedbackId;
                feedbackElement.className = 'invalid-feedback';
                field.parentNode.appendChild(feedbackElement);
            }
            
            feedbackElement.textContent = message;
        } else {
            field.classList.remove('is-invalid');
            if (feedbackElement) {
                feedbackElement.remove();
            }
            
            if (field.value.trim() !== '') {
                field.classList.add('is-valid');
            }
        }
    }

    // Validation avant soumission
    form.addEventListener('submit', function(e) {
        let formIsValid = true;
        let firstInvalidField = null;
        
        Object.keys(validationRules).forEach(fieldId => {
            const field = document.getElementById(fieldId);
            if (field) {
                const isValid = validateField(field, validationRules[fieldId]);
                if (!isValid) {
                    formIsValid = false;
                    if (!firstInvalidField) firstInvalidField = field;
                }
            }
        });
        
        if (!formIsValid) {
            e.preventDefault();
            showToast('Veuillez corriger les erreurs dans le formulaire', 'danger');
            
            if (firstInvalidField) {
                firstInvalidField.scrollIntoView({
                    behavior: 'smooth',
                    block: 'center'
                });
                firstInvalidField.focus();
            }
        }
    });

    function showToast(message, type) {
        const toast = document.createElement('div');
        toast.className = `alert alert-${type} position-fixed top-0 end-0 m-3`;
        toast.style.zIndex = '9999';
        toast.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        
        document.body.appendChild(toast);
        
        setTimeout(() => {
            toast.remove();
        }, 5000);
    }
});