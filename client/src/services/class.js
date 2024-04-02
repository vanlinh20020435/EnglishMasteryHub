import { $axios } from "."

export const getClasses = async (token = null) => {
    const path = '/api/class'
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

export const getClass = async (token = null, id = null) => {
    const path = `/api/class/${id}`
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

export const getStudentsOfClass = async (token = null, id = null) => {
    const path = `/api/class/${id}/students/get-all`
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

export const getTestsOfClass = async (token = null, id = null) => {
    const path = `/api/class/${id}/tests/get-all`
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

export const searchClasses = async (token = null, params) => {
    const path = '/api/class/search'
    var result = {
        success: false,
        data: null
    }
    const healthyParams = {}
    if (params.className) healthyParams.className = params.className
    if (params.teacherId) healthyParams.teacherId = params.teacherId
    try {
        const response = await $axios.get(path
            , { headers: { 'Authorization': 'Bearer ' + token }, params: healthyParams })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
        result.success = false
    }
    return result
}

export const createClass = async (token = null, payload = null) => {
    const path = '/api/class'
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.post(path, payload, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
        result.success = false
    }
    return result
}

export const editClass = async (token = null, id = null, payload = null) => {
    const path = `/api/class/${id}`
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.put(path, payload, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
        result.success = false
    }
    return result
}

export const deleteClass = async (token = null, id = null) => {
    const path = `/api/class/${id}`
    var result = {
        success: false,
        data: null
    }
    try {
        const response = await $axios.delete(path, { headers: { 'Authorization': 'Bearer ' + token } })
        result.data = response.data;
        result.success = true
    } catch (error) {
        console.log(error);
        result.success = false
    }
    return result
}

export const importStudent = async (token = null, id = null, file = null) => {
    const path = `/api/students/import/${id}`
    let result = {
        success: false,
        data: null,
    };
    const formData = new FormData();
    formData.append("file", file);
    try {
        const response = await $axios.post(path, formData, {
            headers: { 'Authorization': 'Bearer ' + token },
        });
        result.data = response.data;
        result.success = true;
    } catch (error) {
        console.log(error);
        result.success = false
    }
    return result;
};