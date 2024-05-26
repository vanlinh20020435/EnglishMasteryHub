describe('do exam', () => {
    beforeEach(() => {
      cy.visit("http://englishmasteryhub.online/login");
    });
  
    it('passes', () => {
      cy.get('#input-0').type('huystudent')
      cy.get('#input-2').type('123123')
      cy.get('.v-btn').click()
      cy.get('.v-snackbar__content').contains('Đăng nhập thành công')
      cy.get(':nth-child(2) > .v-list-item').click()
      cy.get(':nth-child(2) > .v-btn').click()
      cy.get('input[type=password]').type('123123')
      cy.get('.v-card-actions > .bg-success').click()
      cy.get('textarea.v-field__input').type('Hôm nay trời thật đẹp')
      cy.get('.bg-success.v-btn--size-default').click()
      cy.get('.v-card-actions > .bg-success').click()
      cy.get('.v-snackbar__content').contains('Nộp bài thành công')
    })
  
    it('wrong password', () => {
      cy.get('#input-0').type('huystudent')
      cy.get('#input-2').type('123123')
      cy.get('.v-btn').click()
      cy.get('.v-snackbar__content').contains('Đăng nhập thành công')
      cy.get(':nth-child(2) > .v-list-item').click()
      cy.get(':nth-child(2) > .v-btn').click()
      cy.get('input[type=password]').type('123456')
      cy.get('.v-card-actions > .bg-success').click()
      cy.get('.v-snackbar__content').contains('Mật khẩu không chính xác')
    })
  })