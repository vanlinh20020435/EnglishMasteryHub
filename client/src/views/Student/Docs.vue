<template>
    <v-card v-if="isLoading" class="d-flex justify-center">
        <v-progress-circular :size="70" :width="7" color="success" indeterminate></v-progress-circular>
    </v-card>
    <v-list v-else-if="filesInfo?.length" style="padding-top: 0; padding-bottom: 0;">
        <v-hover v-for="file in filesInfo" v-slot="{ isHovering, props }">
            <v-card @click="() => fileClick(file.url)" :class="{ 'on-hover': isHovering }"
                :elevation="isHovering ? 4 : 2" v-bind="props" style="margin: 0 8px 8px">
                <v-list-item :key="file.fileId" :subtitle="file.name" :title="file.documentName">
                    <template v-slot:prepend>
                        <v-avatar color="success">
                            <v-icon color="white">mdi-folder-zip</v-icon>
                        </v-avatar>
                    </template>
                    <template v-slot:append>
                        <div style="display: flex; flex-direction: column; align-items: flex-end;">
                            <v-list-item-title>Loại: {{ file.type }}</v-list-item-title>
                            <v-list-item-subtitle>Url: {{ file.url }}</v-list-item-subtitle>
                        </div>
                    </template>
                </v-list-item>
            </v-card>
        </v-hover>
    </v-list>
    <v-card v-else class="d-flex justify-center">Bạn không có tài liệu nào</v-card>
</template>

<script>
import { getFilesByClass } from '@/services'
import { mapState } from 'pinia';
import { authenticationRole, studentStore } from '@/stores';

export default {
    data: () => ({
        filesInfo: [],
        isLoading: false
    }),
    computed: {
        ...mapState(authenticationRole, ['authentication']),
        ...mapState(studentStore, ['student']),
    },
    async mounted() {
        this.isLoading = true
        const res = await getFilesByClass(this.authentication.accessToken.token, this.student.class.classId)
        if (res.success) {
            this.filesInfo = res.data
        }
        this.isLoading = false
    },
    methods: {
        fileClick(url) {
            window.open(url, "_blank");
        }
    }
}
</script>