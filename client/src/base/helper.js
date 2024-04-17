// Function to remove Vietnamese diacritics
export const removeVietnameseDiacritics = (str) => {
  return str.normalize("NFD").replace(/[\u0300-\u036f]/g, "");
};

export const formatOriginalText = (str) => {
  console.log('str ===', str);
  return str?.trim()?.toLowerCase()?.replace(/\./gi, "")
}