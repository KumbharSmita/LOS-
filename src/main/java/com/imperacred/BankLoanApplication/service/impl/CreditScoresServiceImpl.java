//package com.imperacred.BankLoanApplication.service.impl;
//
//import com.imperacred.BankLoanApplication.dto.CreditScoresDTO;
//import com.imperacred.BankLoanApplication.model.CreditScores;
//import com.imperacred.BankLoanApplication.repository.CreditScoresRepository;
//import com.imperacred.BankLoanApplication.service.CreditScoresService;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDateTime;
//
//@Service
//public class CreditScoresServiceImpl implements CreditScoresService {
//
//    private static final Logger logger = LogManager.getLogger(CreditScoresServiceImpl.class);
//
//    @Autowired
//    private CreditScoresRepository creditScoresRepository;
//
//    @Override
//    public CreditScoresDTO generateAndSaveScore(CreditScoresDTO dto) {
//        logger.info("Generating credit score for lead ID: {}", dto.getLeadsId());
//
//        int score = dto.getScore();
//        String riskCategory = getRiskCategory(score);
//        LocalDateTime fetchedAt = LocalDateTime.now();
//
//        logger.debug("Calculated risk category: {}", riskCategory);
//
//        CreditScores creditScore = new CreditScores();
//        creditScore.setLeadsId(dto.getLeadsId());
//        creditScore.setBureau(dto.getBureau());
//        creditScore.setScore(score);
//        creditScore.setRisk(riskCategory);//risk is auto calculated by score
//        creditScore.setFetchedAt(fetchedAt);
//
//        logger.debug("Saving CreditScores entity: {}", creditScore);
//
//        CreditScores saved = creditScoresRepository.save(creditScore);
//
//        logger.info("Credit score saved successfully for lead ID: {}, DB ID: {}", saved.getLeadsId(), saved.getId());
//
//        return new CreditScoresDTO(
//                saved.getLeadsId(),
//                saved.getBureau(),
//                saved.getScore(),
//                saved.getRisk(),
//                saved.getFetchedAt()
//        );
//    }
//
//    private String getRiskCategory(int score) {
//        if (score >= 750) return "Low";
//        else if (score >= 600) return "Medium";
//        else return "High";
//    }
//
//    @Override
//    public CreditScoresDTO getCreditScoreById(int id) {
//        logger.info("Fetching credit score with ID: {}", id);
//        CreditScores creditScore = creditScoresRepository.findById(id).orElse(null);
//
//        if (creditScore != null) {
//            logger.debug("Credit score found: {}", creditScore);
//            return convertToDTO(creditScore);
//        } else {
//            logger.warn("No credit score found for ID: {}", id);
//            return null;
//        }
//    }
//
//    private CreditScoresDTO convertToDTO(CreditScores entity) {
//        logger.debug("Converting CreditScores entity to DTO for ID: {}", entity.getId());
//
//        CreditScoresDTO dto = new CreditScoresDTO();
//        dto.setLeadsId(entity.getLeadsId());
//        dto.setBureau(entity.getBureau());
//        dto.setScore(entity.getScore());
//        dto.setRisk(entity.getRisk());
//        dto.setFetchedAt(entity.getFetchedAt());
//
//        logger.debug("Converted DTO: {}", dto);
//        return dto;
//    }
//}

package com.imperacred.BankLoanApplication.service.impl;

import org.springframework.stereotype.Service;

import com.imperacred.BankLoanApplication.service.CreditScoresService;

import java.util.Random;

@Service
public class CreditScoresServiceImpl implements CreditScoresService {

    private final Random random = new Random();

    @Override
    public int generateCreditScore(String panNumber) {
        int riskCategory = Math.abs(panNumber.hashCode()) % 3;

        return switch (riskCategory) {
            case 0 -> 500 + random.nextInt(200);  // Low Risk (500–699)
            case 1 -> 700 + random.nextInt(100);  // Medium Risk (700–799)
            default -> 800 + random.nextInt(101); // High Risk (800–900)
        };
    }
}
