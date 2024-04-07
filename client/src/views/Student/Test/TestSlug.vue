<template>
    <v-card v-if="isLoading" class="d-flex justify-center">
        <v-progress-circular :size="70" :width="7" color="success" indeterminate></v-progress-circular>
    </v-card>
    <v-card v-else>
        <div style="display: flex;">
            <div style="flex: 1">
                <v-card-title style="font-size: 1.75rem;">{{ test.testName }}</v-card-title>
                <v-card-subtitle class="d-flex align-center" style="padding-bottom: 4px">
                    <v-icon>mdi-clock-outline</v-icon>{{ test.time }} phút
                    <v-icon style="margin-left: 16px;">mdi-text-box-edit-outline</v-icon>{{ test.totalQuestions }} câu
                </v-card-subtitle>
            </div>
            <div>
                <v-card-text style="flex: 0;padding: 8px 0 4px; min-width: 230px; font-size: 1rem;">
                    Thời gian bắt đầu: {{ test.startDate }}
                </v-card-text>
                <v-card-text style="flex: 0;padding: 0px 0 4px; min-width: 230px; font-size: 1rem;">
                    Thời gian kết thúc: {{ test.endDate }}
                </v-card-text>
            </div>
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
import { getTestByClass } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';

export default {
    data: () => ({
        test: {},
        isLoading: false
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['student']),
    },
    async mounted() {
        this.isLoading = true
        const res = await getTestByClass(this.authentication.accessToken.token, this.student.class.classId, this.$route.params.id)
        if (res.success) {
            this.test = res.data
        }
        this.isLoading = false
    }
}
</script>