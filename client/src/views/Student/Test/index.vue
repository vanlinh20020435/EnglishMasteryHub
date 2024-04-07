<template>
    <v-card v-if="isLoading" class="d-flex justify-center">
        <v-progress-circular :size="70" :width="7" color="success" indeterminate></v-progress-circular>
    </v-card>
    <v-list v-else-if="tests.length" style="padding-top: 0; padding-bottom: 0;">
        <v-hover v-for="test in tests" v-slot="{ isHovering, props }">
            <p style="color: #888;font-size: 14px;text-align: end; margin-right: 8px;">{{ test.startDate }} - {{
        test.endDate }}
            </p>
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
import { getTestInfoByClass } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';

export default {
    data: () => ({
        tests: [],
        isLoading: false
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['student']),
    },
    async mounted() {
        this.isLoading = true
        const res = await getTestInfoByClass(this.authentication.accessToken.token, this.student.class.classId)
        if (res.success) {
            this.tests = res.data
        }
        this.isLoading = false
    }
}
</script>