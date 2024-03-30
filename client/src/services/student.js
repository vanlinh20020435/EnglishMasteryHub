import { $axios } from '.';

export const getStudents = async (token = null) => {
  const path = '/api/students';
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.get(path, {
      headers: { Authorization: 'Bearer ' + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const getStudent = async (token = null, id = null) => {
  const path = `/api/students/find-by-user/${id}`;
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.get(path, {
      headers: { Authorization: 'Bearer ' + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const searchStudents = async (token = null, params) => {
  const path = '/api/students/search';
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
    result.success = false
  }
  return result;
};

export const createStudent = async (token = null, payload = null) => {
  const path = '/api/students';
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.post(path, payload, {
      headers: { Authorization: 'Bearer ' + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const editStudent = async (id = null, token = null, payload = null) => {
  const path = `/api/students/${id}`;
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.put(path, payload, {
      headers: { Authorization: 'Bearer ' + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};

export const editStudentStatus = async (
  id = null,
  token = null,
  updateValue
) => {
  const path = `/api/students/${id}/update-status`;
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
        headers: { Authorization: 'Bearer ' + token },
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

export const changeStudentPassword = async (
  id = null,
  token = null,
  password
) => {
  const path = `/api/students/${id}/update-password`;
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.put(
      path,
      {},
      { params: { password }, headers: { Authorization: 'Bearer ' + token } }
    );
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};
export const deleteStudent = async (token = null, id = null) => {
  const path = `/api/students/${id}`;
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.delete(path, {
      headers: { Authorization: 'Bearer ' + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false
  }
  return result;
};
