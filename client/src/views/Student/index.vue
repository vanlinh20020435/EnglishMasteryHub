<script>
import StudentLayout from '@/layouts/StudentLayout.vue';
import { studentStore, authenticationRole } from '@/stores';
import { mapState } from 'pinia';
import { getStudent } from '@/services';

export default {
    components: {
        StudentLayout
    },
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['updateStudent']),
    },
    data: () => ({
        student: {}
    }),
    async created() {
        console.log('layout');
        const res = await getStudent(this.authentication.accessToken.token, this.authentication.user.userId)
        if (res.success) {
            this.student = res.data
            this.updateStudent(res.data)
        }
    }
}
</script>

<template>
    <StudentLayout :cls="student.class">
        <router-view />
    </StudentLayout>
</template>
