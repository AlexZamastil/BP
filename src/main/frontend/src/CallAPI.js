import axios from "axios";

/**
 * Author: Alex Zamastil
 * Utility file for creating unified API calls.
 */

//standart API call with authentication
export function callAPI(method, requestName, data, xsrfToken){

    return axios({
        method: method,
        url: process.env.REACT_APP_BACKEND_API_URL+requestName,
        data: data,
        withCredentials:true,
        headers: {
                 "Content-Type": "application/json; charset=utf-8",
                         "Accept": "application/json",
                         "Authorization": localStorage.getItem("token"),
                         "X-XSRF-TOKEN": xsrfToken,
                         "Accept-Language": localStorage.getItem("Localization") 
                   }
      })
}
//API call without authentication
export function callAPINoAuth(method, requestName, data, xsrfToken){
   
    return axios({
        method: method,
        url: process.env.REACT_APP_BACKEND_API_URL+requestName,
        data: data,
        withCredentials:true,
        headers: {
                 "Content-Type": "application/json; charset=utf-8",
                         "Accept": "application/json",
                         "X-XSRF-TOKEN": xsrfToken,
                         "Accept-Language": localStorage.getItem("Localization")
                   }
      })
}
//API call with multipart file, the Content type is different, so it must have a separate function
export function callAPIMultipartFile(method, requestName, data, xsrfToken){
    
    return axios({
        method: method,
        url: process.env.REACT_APP_BACKEND_API_URL+requestName,
        data: data,
        withCredentials:true,
        headers: {
                 "Content-Type": "multipart/form-data",
                         "Accept": "application/json",
                         "Authorization": localStorage.getItem("token"),
                         "X-XSRF-TOKEN": xsrfToken,
                         "Accept-Language": localStorage.getItem("Localization")
                   }
      })
}
