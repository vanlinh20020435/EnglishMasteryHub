<script>
import DefaultLayout from '@/layouts/DefaultLayout.vue';

export default {
    components: {
        DefaultLayout
    },
    data: () => ({
        menu: [
            {
                title: 'Quản lí người dùng',
                value: 'user',
                icon: 'mdi-account',
                children: [
                    {
                        title: 'Quản lí Admin',
                        value: '',
                        icon: 'mdi-account-key',
                    },
                    {
                        title: 'Quản lí giáo viên',
                        value: 'teacher',
                        icon: 'mdi-account-tie',
                    },
                    {
                        title: 'Quản lí học sinh',
                        value: 'student',
                        icon: 'mdi-account-group',
                    },
                ]
            },
            {
                title: 'Quản lí lớp học',
                value: 'class',
                icon: 'mdi-google-classroom',
            },
            {
                title: 'Quản lí sự kiện',
                value: 'event',
                icon: 'mdi-calendar-star',
            },
        ],
    }),
    created() {
        this.$router.replace('/admin/user')
    }
}
</script>

<template>
    <DefaultLayout :menu="menu">
        <router-view />
    </DefaultLayout>
</template>
