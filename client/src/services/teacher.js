import { $axios } from '.';

export const getTeachers = async (token) => {
  const path = '/api/teachers';
  return await apiCallerGet(path, token);
};

export const searchTeachers = async (token = null, params) => {
  const path = '/api/teachers/search';
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.get(path, {
      params: {
        username: params.username || '',
        name: params.name || '',
        email: params.email || '',
      },
      headers: { Authorization: 'Bearer ' + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
  }
  return result;
};

export const createTeacher = async (token = null, payload = null) => {
  const path = '/api/teachers';
  return await apiCallerPost(path, token, payload);
};

export const apiCallerGet = async (
  path,
  token = JSON.parse(localStorage?.getItem('accessToken'))?.token
) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const headers = { Accept: '*' };
    if (!!token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    const response = await $axios.get(path, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
  }
  return result;
};

export const apiCallerPost = async (
  path,
  token = JSON.parse(localStorage?.getItem('accessToken'))?.token,
  body
) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const headers = { Accept: '*' };
    if (!!token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    const response = await $axios.post(path, body, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
  }
  return result;
};

export const apiCallerDelete = async (path, body) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const token = JSON.parse(localStorage?.getItem('accessToken'))?.token;
    const headers = { Accept: '*' };
    if (!!token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    const response = await $axios.delete(path, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
  }
  return result;
};

export const apiCallerPut = async (path, body) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const token = JSON.parse(localStorage?.getItem('accessToken'))?.token;
    const headers = { Accept: '*' };
    if (!!token) {
      headers['Authorization'] = `Bearer ${token}`;
    }
    const response = await $axios.put(path, body, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
  }
  return result;
};
