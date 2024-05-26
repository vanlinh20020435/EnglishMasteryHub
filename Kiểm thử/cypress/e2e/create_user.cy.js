describe('template spec', () => {
  beforeEach(() => {
    cy.visit("http://englishmasteryhub.online/login");
  });

  it('passes', () => {
    cy.get('#input-0').type('admin')
    cy.get('#input-2').type('admin')
    cy.get('.v-btn').click()
    cy.get('.v-btn--variant-outlined').click()
    cy.get('.v-row > :nth-child(1) input').type('cyprestest')
    cy.get('.v-row > :nth-child(2) input').type('cyprestest')
    cy.get('.v-row > :nth-child(3) input').type('cyprestest')
    cy.get('.v-row > :nth-child(5) input').type('cyprestest')
    cy.get(':nth-child(6) > .v-input > .v-input__control > .v-field > .v-field__append-inner > .mdi-menu-down').click()
  })
})