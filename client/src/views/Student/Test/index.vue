<template>
    <v-list v-if="tests.length">
        <v-hover v-for="test in tests" v-slot="{ isHovering, props }">
            <v-card @click="$router.replace(`/student/test/${test.testId}`)" :class="{ 'on-hover': isHovering }"
                :elevation="isHovering ? 4 : 2" v-bind="props" style="margin: 0 8px 8px">
                <v-list-item :key="test.testId" :subtitle="test.description" :title="test.testName">
                    <template v-slot:prepend>
                        <v-avatar color="success">
                            <v-icon color="white">mdi-clipboard-text</v-icon>
                        </v-avatar>
                    </template>

                    <template v-slot:append>
                        <div style="display: flex; flex-direction: column; align-items: flex-end;">
                            <v-list-item-title>Thời gian làm bài: {{ test.time }} phút</v-list-item-title>
                            <v-list-item-subtitle>Tổng số câu hỏi: {{ test.totalQuestions }} câu</v-list-item-subtitle>
                        </div>
                    </template>
                </v-list-item>
            </v-card>
        </v-hover>
    </v-list>
    <v-card v-else class="d-flex justify-center">Bạn không có bài kiểm tra nào</v-card>
</template>

<script>
import { getTestsByClass } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';

export default {
    data: () => ({
        tests: []
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['student']),
    },
    async mounted() {
        console.log('test', this.authentication.accessToken.token, this.student.class.classId);
        const res = await getTestsByClass(this.authentication.accessToken.token, this.student.class.classId)
        if (res.success) {
            this.tests = res.data
        }
    }
}
</script>