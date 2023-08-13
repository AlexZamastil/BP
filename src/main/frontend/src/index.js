import React from "react"
import ReactDOM from "react-dom/client"
import App from "./App"
import { BrowserRouter } from "react-router-dom"
import i18next from "i18next"
import { I18nContext, I18nextProvider } from "react-i18next"

const root = ReactDOM.createRoot(document.getElementById("root"))

i18next.init({
  interpolation: { escapeValue: false },
  lng: 'en', // Default language
  resources: {
    en: { translation: require('./locales/en.json') },
    cs: { translation: require('./locales/cs.json') }
  },
});

root.render(
  <React.StrictMode>
    <BrowserRouter>

    <I18nextProvider i18n={i18next}>
    <App/>
    </I18nextProvider>
      
    </BrowserRouter>
  </React.StrictMode>
)