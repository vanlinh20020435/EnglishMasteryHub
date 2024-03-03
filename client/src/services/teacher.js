import { $axios } from ".";

function getTeachers() {
  const path = "/api/user/userinfo";
  $axios.get(path, { headers: { Authorization: "" } });
}

const apiCallerGet = async (
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
  }
  return result;
};

const apiCallerPost = async (
  path,
  token = JSON.parse(localStorage?.getItem("accessToken"))?.token,
  body
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

const apiCallerDelete = async (
  path,
  token = JSON.parse(localStorage?.getItem("accessToken"))?.token,
  body
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
    const response = await $axios.delete(path, body, {
      headers: headers,
    });

    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
  }
  return result;
};

const apiCallerPut = async (
  path,
  token = JSON.parse(localStorage?.getItem("accessToken"))?.token,
  body
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

export {
  getTeachers,
  apiCallerGet,
  apiCallerDelete,
  apiCallerPut,
  apiCallerPost,
};
