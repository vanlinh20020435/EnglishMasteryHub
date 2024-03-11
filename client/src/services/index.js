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
} from './class';
export { login, getUserInfo } from './auth';
export { getTests } from './test';

const $axios = {
  ...axios.create({
    baseURL: import.meta.env.VITE_API,
  }),
};

export { $axios };
