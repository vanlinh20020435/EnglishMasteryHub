import { $axios } from ".";

export const getTeachers = async (token) => {
  const path = "/api/teachers";
  return await apiCallerGet(path, token);
};

export const searchTeachers = async (token = null, params) => {
  const path = "/api/teachers/search";
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.get(path, {
      params: {
        username: params.username || "",
        name: params.name || "",
        email: params.email || "",
      },
      headers: { Authorization: "Bearer " + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const createTeacher = async (token = null, payload = null) => {
  const path = "/api/teachers";
  return await apiCallerPost(path, token, payload);
};

export const apiCallerGet = async (
  path,
  token = JSON.parse(localStorage?.getItem("accessToken"))?.token
) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const headers = { Accept: "*" };
    if (!!token) {
      headers["Authorization"] = `Bearer ${token}`;
    }
    const response = await $axios.get(path, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const apiCallerPost = async (
  path,
  token = JSON.parse(localStorage?.getItem("accessToken"))?.token,
  body
) => {
  let result = {
    success: false,
    data: null,
    msg: "",
  };
  try {
    const headers = { Accept: "*" };
    if (!!token) {
      headers["Authorization"] = `Bearer ${token}`;
    }
    const response = await $axios.post(path, body, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    result.msg = error.response.data.message;
    result.success = false
  }
  return result;
};

export const apiCallerDelete = async (path, body) => {
  let result = {
    success: false,
    data: null,
    msg: "",
  };
  try {
    const token = JSON.parse(localStorage?.getItem("accessToken"))?.token;
    const headers = { Accept: "*" };
    if (!!token) {
      headers["Authorization"] = `Bearer ${token}`;
    }
    const response = await $axios.delete(path, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    result.msg = error.response.data.message;
    result.success = false
  }
  return result;
};

export const apiCallerPut = async (path, body) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const token = JSON.parse(localStorage?.getItem("accessToken"))?.token;
    const headers = { Accept: "*" };
    if (!!token) {
      headers["Authorization"] = `Bearer ${token}`;
    }
    const response = await $axios.put(path, body, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const editTeacher = async (id = null, token = null, payload = null) => {
  const path = "/api/teachers";
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.put(path + `/${id}`, payload, {
      headers: { Authorization: "Bearer " + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const editTeacherStatus = async (
  id = null,
  token = null,
  updateValue
) => {
  const path = `/api/teachers/${id}/update-status`;
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.put(
      path,
      {},
      {
        params: { status: updateValue },
        headers: { Authorization: "Bearer " + token },
      }
    );
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const changeTeacherPassword = async (
  id = null,
  token = null,
  password
) => {
  const path = `/api/teachers/${id}/update-password`;
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.put(
      path,
      {},
      { params: { password }, headers: { Authorization: "Bearer " + token } }
    );
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};
export const deleteTeacher = async (token = null, id = null) => {
  const path = `/api/teachers/${id}`;
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.delete(path, {
      headers: { Authorization: "Bearer " + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};
