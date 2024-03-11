<script>
import StudentLayout from '@/layouts/StudentLayout.vue';

export default {
    components: {
        StudentLayout
    },
}
</script>

<template>
    <StudentLayout>
        <router-view />
    </StudentLayout>
</template>
