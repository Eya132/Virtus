<?php
// src/Form/UserType.php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\EmailType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\Regex;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Form\FormInterface;

class UserType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $isEdit = $options['is_edit'] ?? false;
        
        $builder
            ->add('email_user', EmailType::class, [
                'label' => 'Email',
                'attr' => [
                    'class' => 'form-control',
                ],
            ])
            ->add('password_user', PasswordType::class, [
                'label' => 'Mot de passe',
                'required' => !$isEdit, // Obligatoire seulement en création
                'attr' => [
                    'class' => 'form-control',
                    'minlength' => 6,
                    'data-password-validation' => 'true'
                ],
                'constraints' => $isEdit ? [] : [
                    new NotBlank([
                        'message' => 'Le mot de passe est obligatoire',
                        'groups' => ['registration']
                    ]),
                    new Length([
                        'min' => 6,
                        'minMessage' => 'Le mot de passe doit contenir au moins {{ limit }} caractères',
                        'groups' => ['registration']
                    ])
                ]
            ])
            ->add('nom_user', TextType::class, [
                'label' => 'Nom',
                'empty_data' => '',
                'attr' => ['class' => 'form-control']
            ])
            ->add('prenom_user', TextType::class, [
                'label' => 'Prénom',
                'empty_data' => '',
                'attr' => ['class' => 'form-control']
            ])
            ->add('sexe_user', ChoiceType::class, [
                'label' => 'Sexe',
                'choices' => [
                    'Male' => 'Male',
                    'Female' => 'Female'
                ],
                'attr' => ['class' => 'form-control']
            ])
            ->add('telephone_user', TextType::class, [
                'label' => 'Téléphone',
                'empty_data' => '',
                'attr' => [
                    'class' => 'form-control',
                    'placeholder' => 'ooredoo, orange, telecom.',
                ]
            ])
            ->add('description_user', TextareaType::class, [
                'label' => 'Description',
                'empty_data' => '',
                'required' => false,
                'attr' => ['class' => 'form-control', 'rows' => 3]
            ])
            ->add('adresse_user', TextType::class, [
                'label' => 'Adresse',
                'empty_data' => '',
                'attr' => ['class' => 'form-control']
            ])
            ->add('role', ChoiceType::class, [
                'label' => 'Rôle',
                'choices' => [
                    'PLAYER' => 'PLAYER',
                    'ADMIN' => 'ADMIN',
                    'NUTRITIONIST' => 'NUTRITIONIST'
                ],
                'attr' => ['class' => 'form-control']
            ])
            ->add('experience', ChoiceType::class, [
                'label' => 'Expérience',
                'choices' => [
                    '1 an' => 'ONE_YEAR',
                    '2 ans' => 'TWO_YEARS',
                    '3 ans' => 'THREE_YEARS',
                    '4 ans et plus' => 'FOUR_YEARS_PLUS'
                ],
                'attr' => ['class' => 'form-control']
            ])
            ->add('salaire', NumberType::class, [
                'label' => 'Salaire',
                'empty_data' => '',
                'attr' => [
                    'class' => 'form-control',
                    'min' => 0
                ]
            ])
            ->add('niveau_joueur', ChoiceType::class, [
                'label' => 'Niveau du joueur',
                'choices' => [
                    'Débutant' => 'Debutant',
                    'Intermédiaire' => 'Intermediaire',
                    'Expert' => 'Expert'
                ],
                'attr' => ['class' => 'form-control']
            ])
            ->add('max_distance_user', NumberType::class, [
                'label' => 'Distance maximale (km)',
                'empty_data' => '',
                'required' => false,
                'attr' => [
                    'min' => 0,
                    'class' => 'form-control'
                ]
            ])
            ->add('is_premium', ChoiceType::class, [
                'label' => 'Premium',
                'choices' => [
                    'Oui' => true,
                    'Non' => false
                ],
                'attr' => ['class' => 'form-control']
            ])
            ->add('photo_user', FileType::class, [
                'label' => 'Photo de profil',
                'required' => false,
                'mapped' => false,
                'attr' => ['class' => 'form-control']
            ])
            ->add('piece_jointe', FileType::class, [
                'label' => 'Pièce jointe',
                'required' => false,
                'mapped' => false,
                'attr' => ['class' => 'form-control']
            ])
            ->add('date_naissance_user', DateType::class, [
                'label' => 'Date de naissance',
                'empty_data' => '',
                'widget' => 'single_text',
                'attr' => ['class' => 'form-control']
            ])
            ->add('image', FileType::class, [
                'label' => 'Image (JPG, PNG)',
                'mapped' => false,
                'required' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '2M',
                        'mimeTypes' => [
                            'image/jpeg',
                            'image/png',
                        ],
                        'mimeTypesMessage' => 'Veuillez télécharger une image valide (JPG ou PNG)',
                    ]),
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => User::class,
            'is_edit' => false,
            'validation_groups' => ['Default']
        ]);
    }
}