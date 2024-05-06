import { $axios } from '.';

export const getTests = async (token = null) => {
  const path = '/api/testss';
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
    result.success = false;
  }
  return result;
};

export const getTest = async (token = null, testId = null) => {
  const path = `/api/testss/${testId}`;
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
    result.success = false;
  }
  return result;
};

export const getTestByClass = async (
  token = null,
  classId = null,
  testId = null
) => {
  const path = `/api/class/${classId}/tests-info/${testId}`;
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
    result.success = false;
  }
  return result;
};

export const getTestByStudent = async (
  token = null,
  testId = null,
  classId = null,
  password
) => {
  const path = `/api/testss/verify?password=${password}&testId=${testId}&classId=${classId}`;
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
    result.success = false;
  }
  return result;
};

export const getTestResults = async (token = null, classId = null, testId = null, studentId = null) => {
  const path = `/api/test-result/statistic/${studentId}?testId=${testId}&classId=${classId}`;
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
    result.success = false;
  }
  return result;
};

export const getTestInfoByClass = async (token = null, classId) => {
  const path = `/api/class/${classId}/tests-info/get-all`;
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
    result.success = false;
  }
  return result;
};

export const searchAdmins = async (token = null, params) => {
  const path = '/api/admins/search';
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
    result.success = false;
  }
  return result;
};

export const createAdmin = async (token = null, payload = null) => {
  const path = '/api/admins';
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
    result.success = false;
  }
  return result;
};

export const editAdmin = async (id = null, token = null, payload = null) => {
  const path = '/api/admins';
  var result = {
    success: false,
    data: null,
  };
  try {
    const response = await $axios.put(path + `/${id}`, payload, {
      headers: { Authorization: 'Bearer ' + token },
    });
    result.data = response.data;
    result.success = true;
  } catch (error) {
    console.log(error);
    result.success = false;
  }
  return result;
};

export const editAdminStatus = async (id = null, token = null, updateValue) => {
  const path = `/api/admins/${id}/update-status`;
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
    result.success = false;
  }
  return result;
};

export const changeAdminPassword = async (
  id = null,
  token = null,
  password
) => {
  const path = `/api/admins/${id}/update-password`;
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
    result.success = false;
  }
  return result;
};

export const submitExam = async (token = null, payload = null) => {
  const path = '/api/test-result';
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
    result.success = false;
  }
  return result;
};
