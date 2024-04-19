<template>
    <v-card color="success" style="height: 250px; z-index: 0;">

    </v-card>
    <div :class="`class-wrapper ${$vuetify.display.smAndDown ? 'sm' : ''}`">
        <v-avatar class="class-avatar">
            <v-img class="class-avatar-img" alt="Avatar" :src="getClassAvatar(student?.class)"></v-img>
        </v-avatar>
        <v-card class="class-info">
            <div class="class-info__main">
                <v-card-title class="class-name">{{ student.class.className }}</v-card-title>
                <div class="class-sub">
                    <v-card-subtitle class="class-subtitle"><v-icon>mdi-human-male-board</v-icon>{{
        student.class.teacher.name }}</v-card-subtitle>
                    <v-card-subtitle class="class-subtitle"><v-icon>mdi-calendar-range</v-icon>{{
        student.class.startDate }} - {{
        student.class.endDate }}</v-card-subtitle>
                </div>
            </div>
            <v-card-text class="class-des">Mô tả: {{ student.class.description }}</v-card-text>
        </v-card>
    </div>
    <v-card style="margin-bottom: 16px;">
        <v-card-title>Thông báo</v-card-title>
        <v-card-text>
            <Notification />
        </v-card-text>
    </v-card>
    <v-card>
        <v-card-title>Danh sách học sinh</v-card-title>
        <v-card-text>
            <v-data-table :loading="isLoadingStudent" :headers="studentsHeaders" :items="students"
                :sort-by="[{ key: 'studentId', order: 'asc' }]">
                <template v-slot:item.avatar="{ item }">
                    <v-avatar>
                        <v-img alt="Avatar" :src="item.avatar || getAvtUserMethod(item)"></v-img>
                    </v-avatar>
                </template>

                <template v-slot:item.gender="{ item }">
                    {{ item.gender ? 'Male' : 'Female' }}
                </template>
            </v-data-table>
        </v-card-text>
    </v-card>
</template>

<script>
import { getStudentsOfClass } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';
import Notification from '@/views/Student/Notification/index.vue';
import classDefaultImage from "@/assets/images/class.png";
import { getAvtUser } from '@/base/helper';

export default {
    components: {
        Notification
    },
    data: () => ({
        test: {},
        students: [],
        isLoadingStudent: false,
        studentsHeaders: [
            {
                title: 'Student ID',
                align: 'center',
                key: 'studentId',
                sortable: false,
            },
            { title: 'Avatar', key: 'avatar', sortable: false },
            { title: 'Name', key: 'name', sortable: false },
            { title: 'Birthday', key: 'birthday', sortable: false },
            { title: 'Gender', key: 'gender', sortable: false },
        ],
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['student'])
    },
    async mounted() {
        await this.getStudents()
    },
    methods: {
        async getStudents() {
            const res = await getStudentsOfClass(this.authentication?.accessToken?.token, this.student.class.classId)
            if (res.success) {
                this.students = res.data
            }
        },
        getClassAvatar(classData) {
        return classData && classData.avatar
            ? classData.avatar
            : classDefaultImage; // Return the class avatar or the default image
        },
        getAvtUserMethod(item) {
            return getAvtUser(item, 'student');
        }
    }
}
</script>

<style scoped>
.class-wrapper {
    display: flex;
    flex-direction: column;
}

.class-wrapper .class-avatar {
    width: max-content;
    height: max-content;
    margin: -120px 0 16px 48px;
    z-index: 1;
}

.class-wrapper .class-avatar-img {
    height: 150px;
    width: 150px;
}

.class-wrapper .class-info {
    width: calc(100% - 64px);
    margin: -80px 0 16px 32px;
}

.class-wrapper .class-info__main {
    display: flex;
    align-items: center;
}

.class-wrapper .class-name {
    margin-left: 150px;
    font-size: 40px;
    padding: 16px;
}

.class-wrapper .class-sub {
    margin-top: 8px;
    flex: 1;
}

.class-wrapper .class-subtitle {
    font-size: 16px;
}


.class-wrapper .class-des {
    font-size: 20px;
    margin-left: 8px;
}

/* sm */
.class-wrapper.sm {
    align-items: center;
}

.class-wrapper.sm .class-avatar {
    margin-left: 0px;
}

.class-wrapper.sm .class-avatar-img {
    height: 150px;
    width: 150px;
}

.class-wrapper.sm .class-info {
    padding-top: 60px;
    margin-left: 0;
}

.class-wrapper.sm .class-info__main {
    flex-direction: column;
}

.class-wrapper.sm .class-name {
    margin-left: 0;
    flex: 1;
    padding-left: 0;
    padding-right: 0;
}

.class-wrapper.sm .class-sub {
    display: flex;
    justify-content: center;
}
</style>