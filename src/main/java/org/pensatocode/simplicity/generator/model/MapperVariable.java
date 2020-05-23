package org.pensatocode.simplicity.generator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapperVariable {
    private String name;
    private String getterMethod;
    private String typeGetter;
    private String typeGetterSuffix;
    private String databaseType;
}
