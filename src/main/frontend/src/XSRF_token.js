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