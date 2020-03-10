Feature: User management

    Scenario: Retrieve administrator user
        When I search user 'admin'
        Then the user is found
        And his last name is 'Administrator'

    Scenario: Retrieve system user
        When I search user 'system'
        Then the user is found
        And his last name is 'System'
