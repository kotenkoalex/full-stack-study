package com.kotenko.fullstack.customer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getInt("id")).thenReturn(1);
        Mockito.when(resultSet.getInt("age")).thenReturn(19);
        Mockito.when(resultSet.getString("name")).thenReturn("Alex");
        Mockito.when(resultSet.getString("email")).thenReturn("alex@gmail.com");
        Customer actual = customerRowMapper.mapRow(resultSet, 1);
        Customer expected = new Customer(1, "Alex", "alex@gmail.com", 19);
        assertThat(actual).isEqualTo(expected);
    }
}