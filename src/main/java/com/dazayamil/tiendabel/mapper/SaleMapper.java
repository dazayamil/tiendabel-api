package com.dazayamil.tiendabel.mapper;

import com.dazayamil.tiendabel.dto.response.SaleResponseDTO;
import com.dazayamil.tiendabel.model.entity.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    @Mapping(source = "id", target = "saleId")
    SaleResponseDTO toResponseDTO(Sale sale);
}
