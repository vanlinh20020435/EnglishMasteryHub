<template>
    <v-list-item :key="noti.testId" :subtitle="noti.description" :title="noti.testName">
        <template v-slot:prepend>
            <v-avatar color="success">
                <v-icon color="white">mdi-clipboard-text</v-icon>
            </v-avatar>
        </template>
        <template v-slot:append>
            <div style="display: flex; flex-direction: column; align-items: flex-end;">
                <v-list-item-title>Thời gian làm bài: {{ noti.time }} phút</v-list-item-title>
                <v-list-item-subtitle>Tổng số câu hỏi: {{ noti.totalQuestions }} câu</v-list-item-subtitle>
            </div>
        </template>
    </v-list-item>
</template>

<script>
export default {
    props: {
        noti: Object
    }
}
</script>