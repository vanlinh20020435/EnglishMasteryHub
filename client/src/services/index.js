import axios from 'axios';
export {
  getAdmins,
  searchAdmins,
  createAdmin,
  editAdmin,
  editAdminStatus,
  changeAdminPassword,
  deleteAdmin,
} from './admin';
export {
  getTeachers,
  searchTeachers,
  createTeacher,
  editTeacher,
  editTeacherStatus,
  changeTeacherPassword,
  deleteTeacher,
} from './teacher';
export {
  getStudents,
  getStudent,
  searchStudents,
  createStudent,
  editStudent,
  editStudentStatus,
  changeStudentPassword,
  deleteStudent,
} from './student';
export {
  getClasses,
  getClass,
  getStudentsOfClass,
  getTestsOfClass,
  searchClasses,
  createClass,
  editClass,
  deleteClass,
  importStudent,
  addTestToClass,
  deleteTestInClass,
  getFilesByClass,
} from './class';
export { login, getUserInfo } from './auth';
export {
  getTests,
  getTest,
  getTestByClass,
  getTestInfoByClass,
  getTestByStudent,
  submitExam,
} from './test';

export const uploadFile = async (token, file) => {
  let result = {
    success: false,
    data: null,
  };
  try {
    const path = '/upload';
    const formData = new FormData();
    formData.append('file', file);
    const response = await $axios.post(path, formData, {
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

const $axios = {
  ...axios.create({
    baseURL: import.meta.env.VITE_API,
  }),
};

export { $axios };
