<template>
    <v-list-item class="pt-3 pb-3" :key="noti.testId" :subtitle="noti.description" :title="noti.testName">
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

<style>
    .v-list-item .v-list-item__content .v-list-item-subtitle,
    .v-list-item .v-list-item-subtitle {
        overflow: visible;
        padding-bottom: 0.25rem;
    }
</style>