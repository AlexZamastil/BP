/**
 * Author: Alex Zamastil
 * This function reads the XSRF token value from cookie. This token is needed for authorization some requests.
 */

export default function getXSRFtoken() {
    const cookies = document.cookie.split(';');
    for (const cookie of cookies) {
        const [cookieName, cookieValue] = cookie.trim().split('=');
        if (cookieName === 'XSRF-TOKEN') {
            return cookieValue;
            
        }
    }

    return null;
}