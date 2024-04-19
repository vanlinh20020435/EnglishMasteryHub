
import avtStudentMale from "@/assets/images/avatar_student_male.jpg";
import avtStudentFemale from "@/assets/images/avatar_student_female.jpg";
import avtTeacherFemale from "@/assets/images/avatar_teacher_female.jpg";
import avtTeacherMale from "@/assets/images/avatar_teacher_male.jpg";

// Function to remove Vietnamese diacritics
export const removeVietnameseDiacritics = (str) => {
  return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
};

// Format Original Text
export const formatOriginalText = (str) => {
  console.log('str ===', str);
  return str?.trim()?.toLowerCase()?.replace(/\./gi, "")
}

// Format Two Number
export const formatTwoNumber = (num) => {
  return num >= 10 ? num : "0" + num;
};

// Get Avt User
export const getAvtUser = (userInfo, role) => {
  let avatar = '';
  switch (role || userInfo?.role?.toLowerCase()) {
      case 'student':
          avatar = userInfo?.gender ? avtStudentMale : avtStudentFemale;
          break;
      case 'teacher':
      case 'admin':
          avatar = avtTeacherMale; // Assuming male avatar for teacher and admin
          if (!userInfo?.gender) {
              avatar = avtTeacherFemale; // Change to female avatar if gender is female
          }
          break;
      default:
          // Handle other roles if needed
          break;
  }

  return avatar;
}