<template>
    <v-card>
        <v-card-title>
            {{ question.title }}
        </v-card-title>
        <v-card-text>
            <v-row>
                <v-col md="12" lg="6" v-for="(sub, idx) in question.subQuestions" click>
                    <p style="margin-bottom: 4px">{{ idx + 1 }}.</p>
                    <Audio :file="sub.files[0].url" color="success"></Audio>
                </v-col>
                <v-col lg="6">
                    <v-row>
                        <v-col md="6">
                            <v-checkbox v-model="test" label="T1" value="t1"></v-checkbox>
                        </v-col>
                        <v-col md="6">
                            <v-checkbox v-model="test" label="T2" value="t2"></v-checkbox>
                        </v-col>
                    </v-row>
                </v-col>
            </v-row>
        </v-card-text>
    </v-card>
</template>

<script>
import Audio from '@/components/Audio.vue';

export default {
    components: {
        Audio,
    },
    props: {
        question: Object,
    },
    data: () => ({
        test: 't1'
    }),
    mounted() {
    },
};
</script>
