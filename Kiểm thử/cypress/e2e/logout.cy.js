describe('logout', () => {
    beforeEach(() => {
      cy.visit("http://englishmasteryhub.online/login");
    });
  
    it('passes', () => {
      cy.get('#input-0').type('huystudent')
      cy.get('#input-2').type('123123')
      cy.get('.v-btn').click()
      cy.get('.v-snackbar__content').contains('Đăng nhập thành công')
      cy.get('button .v-avatar').click()
      cy.get('.mx-auto > :nth-child(6)').click()
      cy.get('.card-popup .btn-gradient').click()
      cy.url().should("eq", "http://englishmasteryhub.online/login")
    })
  })