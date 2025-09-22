package ra.edu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ra.edu.repo.EnrollmentLessonRepository;
import ra.edu.service.EnrollmentLessonService;
@Service
public class EnrollmentLessonImpl implements EnrollmentLessonService {
    @Autowired
    private EnrollmentLessonRepository enrollmentLessonRepository;

}
