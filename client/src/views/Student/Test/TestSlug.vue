<template>
    <v-card>
        <div style="display: flex;">
            <div style="flex: 1">
                <v-card-title style="font-size: 1.75rem;">{{ test.testName }}</v-card-title>
                <v-card-subtitle class="d-flex align-center" style="padding-bottom: 4px">
                    <v-icon>mdi-clock-outline</v-icon>{{ test.time }} phút
                    <v-icon style="margin-left: 16px;">mdi-text-box-edit-outline</v-icon>{{ test.totalQuestions }} câu
                </v-card-subtitle>
            </div>
            <v-card-text style="flex: 0;padding: 8px 0 4px; min-width: 230px; font-size: 1rem;">
                Thời gian bắt đầu: {{ test.created }}
            </v-card-text>
        </div>
        <v-divider></v-divider>
        <v-card-text style="font-size: 1rem;">
            {{ test.description }}
        </v-card-text>
        <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" variant="elevated" style="font-weight: 500;">
                Làm bài
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<script>
import { getTestInfo } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';

export default {
    data: () => ({
        test: {}
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['student']),
    },
    async mounted() {
        const res = await getTestInfo(this.authentication.accessToken.token, this.student.class.classId, this.$route.params.id)
        if (res.success) {
            this.test = res.data
        }
    }
}
</script>