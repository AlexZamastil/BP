import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { BrowserRouter } from "react-router-dom";
import i18next from "i18next";
import { I18nextProvider } from "react-i18next";
import { LocalizationProvider } from '@mui/x-date-pickers';
import { AdapterDayjs } from '@mui/x-date-pickers/AdapterDayjs';
import theme from './Theme'; 
import { ThemeProvider } from '@mui/material/styles';

/**
 * Author: Alex Zamastil
 * Index file
 */


// Creating a root element for rendering React components
const root = ReactDOM.createRoot(document.getElementById("root"));

// setting up locale
const storedLocale = localStorage.getItem("locale"); 
const defaultLocale = storedLocale || 'cs';

// Initializing i18next with resources and default language
i18next.init({
  interpolation: { escapeValue: false },
  lng: defaultLocale, 
  resources: {
    en: { translation: require('./locales/en.json') },
    cs: { translation: require('./locales/cs.json') }
  },
});

// Rendering the main application wrapped with providers for localization, theming, and routing
  root.render (
    <React.StrictMode>
      <BrowserRouter>
        <ThemeProvider theme={theme}>
          <LocalizationProvider dateAdapter={AdapterDayjs}>
            <I18nextProvider i18n={i18next}>
              <App/>
            </I18nextProvider>
          </LocalizationProvider>
        </ThemeProvider>
      </BrowserRouter>
    </React.StrictMode>
  );