import './bootstrap.js';
import { Turbo } from '@hotwired/turbo-rails';
/*
 * Welcome to your app's main JavaScript file!
 *
 * This file will be included onto the page via the importmap() Twig function,
 * which should already be in your base.html.twig.
 */
import './styles/app.css';

Turbo.session.drive = false;

console.log('Turbo Drive a été désactivé');

console.log('This log comes from assets/app.js - welcome to AssetMapper! 🎉');
