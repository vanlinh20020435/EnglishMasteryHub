<script>
import DefaultLayout from '@/layouts/DefaultLayout.vue';

export default {
    components: {
        DefaultLayout
    },
    data: () => ({
        menu: [
            {
                title: 'Foo',
                value: 'foo',
                icon: '',
                children: [
                    {
                        title: 'Fizz',
                        value: 'fizz',
                        icon: '',
                    },
                    {
                        title: 'Buzz',
                        value: 'buzz',
                        icon: '',
                    },
                ]
            },
            {
                title: 'Bar',
                value: 'bar',
                icon: '',
            },
        ],
    }),
}
</script>

<template>
    <DefaultLayout :menu="menu">
        <div style="width: 100%; height: 100%;">teacher</div>
        <router-view />
    </DefaultLayout>
</template>
