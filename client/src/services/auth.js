import { $axios } from "."

const login = async (username = null, password = null) => {
    const path = '/api/auth/signin'
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.post(path, { username, password })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
        result.success = false
    }
    return result
}


const getUserInfo = async (token = null) => {
    const path = '/api/user/userinfo'
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.get(path, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
        result.success = false
    }
    return result

}

export { login, getUserInfo }