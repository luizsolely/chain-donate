package com.chaindonate.api.mapper;

import com.chaindonate.api.dto.DonationRequestDTO;
import com.chaindonate.api.dto.DonationResponseDTO;
import com.chaindonate.api.dto.SimpleDonationDTO;
import com.chaindonate.api.entity.Donation;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DonationMapper {

    DonationMapper INSTANCE = Mappers.getMapper(DonationMapper.class);

    SimpleDonationDTO toSimpleDto(Donation donation);

    Donation toEntity(DonationRequestDTO dto);

    DonationResponseDTO toDto(Donation donation);
}
