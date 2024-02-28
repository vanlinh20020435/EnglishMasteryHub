<script>
import TheWelcome from '../components/TheWelcome.vue'
import DefaultLayout from '../layouts/DefaultLayout.vue';

export default {
  components: {
    DefaultLayout
  },
  data: () => ({
    menu: [
      {
        title: 'Foo',
        value: 'foo',
        children: [
          {
            title: 'Fizz',
            value: 'fizz',
          },
          {
            title: 'Buzz',
            value: 'buzz',
          },
        ]
      },
      {
        title: 'Bar',
        value: 'bar',
      },
    ],
  }),
}
</script>

<template>
  <DefaultLayout :menu="menu">
    <TheWelcome />
  </DefaultLayout>
</template>
