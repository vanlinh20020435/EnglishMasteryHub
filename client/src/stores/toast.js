import { ref } from 'vue'
import { defineStore } from 'pinia'

export const toastStore = defineStore('toast', () => {
    const toast = ref({
        message: null,
        type: null,
        active: false
    })
    function updateToast(type, message) {
        toast.value = {
            type,
            message,
            active: true
        }
    }
    function clearToast() {
        toast.value = {
            type: null,
            message: null,
            active: false
        }
    }

    return { toast, updateToast, clearToast }
})
