describe('template spec', () => {
  beforeEach(() => {
    cy.visit("http://englishmasteryhub.online/login");
  });

  it('passes', () => {
    cy.visit('http://englishmasteryhub.online/login')
    cy.get('#input-0').type('admin')
    cy.get('#input-2').type('admin')
    cy.get('.v-btn').click()
    cy.get('.v-snackbar__content').contains('Đăng nhập thành công')
  })

  it('not-passes', () => {
    cy.visit('http://englishmasteryhub.online/login')
    cy.get('#input-0').type('admin')
    cy.get('#input-2').type('123456')
    cy.get('.v-btn').click()
    cy.get('.v-snackbar__content').contains('Đăng nhập thất bại')
  })

  it('empty', () => {
    cy.visit('http://englishmasteryhub.online/login')
    cy.get('.v-btn').click()
    cy.get('#input-0-messages > .v-messages__message').contains('Name is required')
    cy.get('#input-2-messages > .v-messages__message').contains('Name is required')
  })
})