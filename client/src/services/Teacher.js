import { $axios } from ".";

function getTeachers() {
  const path = "/api/user/userinfo";
  $axios.get(path, { headers: { Authentication: "" } });
}

const apiCaller = async (
  path,
  token = JSON.parse(localStorage?.getItem("accessToken"))?.token
) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const headers = { Accept: "application/json" };
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
  console.log("result ===", result);
  return result;
};

export { getTeachers, apiCaller };
