describe('template spec', () => {
  beforeEach(() => {
    cy.visit("http://englishmasteryhub.online/login");
  });

  it('passes', () => {
    cy.get('#input-0').type('admin')
    cy.get('#input-2').type('admin')
    cy.get('.v-btn').click()
    cy.get('.v-btn--variant-outlined').click()
  })
})