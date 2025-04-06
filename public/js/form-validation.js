document.addEventListener('DOMContentLoaded', function() {
    const form = document.querySelector('form[name="user"]');
    if (!form) return;

    // Configuration des règles de validation
    const validationRules = {
        'user_email_user': {
            required: true,
            pattern: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
            message: "Veuillez entrer une adresse email valide"
        },
        'user_password_user': {
        required: !form.dataset.isEdit, // Obligatoire seulement en création
        pattern: /^(?=.*\d).{6,}$/,
        message: "Le mot de passe doit contenir au moins 6 caractères dont un chiffre",
        validate: function(value, isEdit) {
            // Si en mode édition et champ vide -> valide
            // Si en mode création ou champ rempli -> appliquer les règles
            return (isEdit && value === '') || 
                   (!isEdit && value !== '' && /^(?=.*\d).{6,}$/.test(value));
        }
    },
        'user_nom_user': {
            required: true,
            message: "Le nom est obligatoire"
        },
        'user_prenom_user': {
            required: true,
            message: "Le prénom est obligatoire"
        },
        'user_telephone_user': {
            required: true,
            pattern: /^[259]\d{7,}$/,
            message: "Le téléphone doit commencer par 2, 5 ou 9 et contenir 8 chiffres"
        },
        'user_salaire': {
            validate: value => value === '' || parseFloat(value) >= 0,
            message: "Le salaire ne peut pas être négatif"
        },
        'user_max_distance_user': {
            validate: value => value === '' || parseInt(value) >= 0,
            message: "La distance ne peut pas être négative"
        }
    };

    // Fonction pour valider un champ spécifique
    function validateField(field) {
        const rules = validationRules[field.id];
        if (!rules) return true; // Si pas de règles, on considère valide
        
        const value = field.value.trim();
        const isEdit = form.dataset.isEdit === 'true';
        let isValid = true;
        let message = '';
        
        if (rules.required && value === '' && !isEdit) {
            isValid = false;
            message = 'Le mot de passe est obligatoire';
        } else if (value !== '') {
            if (rules.pattern && !rules.pattern.test(value)) {
                isValid = false;
                message = rules.message;
            } else if (rules.validate && !rules.validate(value , isEdit)) {
                isValid = false;
                message = rules.message;
            }
        }
        
        // Mise à jour de l'affichage
        if (!isValid) {
            field.classList.add('is-invalid');
            field.classList.remove('is-valid');
            
            let errorElement = field.nextElementSibling;
            if (!errorElement || !errorElement.classList.contains('invalid-feedback')) {
                errorElement = document.createElement('div');
                errorElement.className = 'invalid-feedback';
                field.parentNode.insertBefore(errorElement, field.nextSibling);
            }
            errorElement.textContent = message;
        } else {
            field.classList.remove('is-invalid');
            field.classList.add('is-valid');
            const errorElement = field.nextElementSibling;
            if (errorElement && errorElement.classList.contains('invalid-feedback')) {
                errorElement.remove();
            }
        }
        
        return isValid;
    }

    // Validation en temps réel
    Object.keys(validationRules).forEach(fieldId => {
        const field = document.getElementById(fieldId);
        if (field) {
            field.addEventListener('blur', () => validateField(field));
            if (fieldId !== 'user_password_user' || !form.dataset.isEdit) {
                field.addEventListener('input', () => validateField(field));
            }
        }
    });

    // Validation séquentielle avant soumission
    form.addEventListener('submit', function(e) {
        let formIsValid = true;
        let firstInvalidField = null;
        
        Object.keys(validationRules).forEach(fieldId => {
            const field = document.getElementById(fieldId);
            if (field && !validateField(field) && formIsValid) {
                formIsValid = false;
                firstInvalidField = field;
            }
        });
        
        if (!formIsValid) {
            e.preventDefault();
            firstInvalidField.scrollIntoView({ behavior: 'smooth', block: 'center' });
            firstInvalidField.focus();
        }
    });
});