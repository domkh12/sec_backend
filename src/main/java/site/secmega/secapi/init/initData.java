package site.secmega.secapi.init;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import site.secmega.secapi.base.DepartmentStatus;
import site.secmega.secapi.base.ProductionLineStatus;
import site.secmega.secapi.base.UserStatus;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.buyer.BuyerRepository;
import site.secmega.secapi.feature.color.ColorRepository;
import site.secmega.secapi.feature.defectType.DefectTypeRepository;
import site.secmega.secapi.feature.department.DepartmentRepository;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.role.RoleRepository;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.time.TimeRepository;
import site.secmega.secapi.feature.tv.TvRepository;
import site.secmega.secapi.feature.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class initData {

    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TvRepository tvRepository;
    private final SizeRepository sizeRepository;
    private final ColorRepository colorRepository;
    private final DepartmentRepository departmentRepository;
    private final ProductionLineRepository productionLineRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final BuyerRepository buyerRepository;
    private final TimeRepository timeRepository;

//    @PostConstruct
    public void init(){
        try {
            initDefectTypeData();
            initDepartment();
            initProductionLine();
            initBuyer();
            initRole();
            initUser();
            initTv();
            initSize();
            initColor();
            initTimeData();
        } catch (Exception e) {
            System.err.println("Error during initializations: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void initDefectTypeData(){
        DefectType defectType1 = new DefectType();
        defectType1.setName("ថ្នេរធ្លាក់ទឹក/ 1/6 Drop Stitches");
        defectTypeRepository.save(defectType1);
        DefectType defectType2 = new DefectType();
        defectType2.setName("ថ្នេរជ្រួញ/ Puckering");
        defectTypeRepository.save(defectType2);
        DefectType defectType3 = new DefectType();
        defectType3.setName("រមូល/ Twisted");
        defectTypeRepository.save(defectType3);
        DefectType defectType4 = new DefectType();
        defectType4.setName("លោតអំបោះ/ Skip Stitches");
        defectTypeRepository.save(defectType4);
        DefectType defectType5 = new DefectType();
        defectType5.setName("ដាច់ថ្នេរ/ Broken");
        defectTypeRepository.save(defectType5);
        DefectType defectType6 = new DefectType();
        defectType6.setName("ថ្នេរបើក/ Open Seam");
        defectTypeRepository.save(defectType6);
        DefectType defectType7 = new DefectType();
        defectType7.setName("ដេរគៀប/ Pleated");
        defectTypeRepository.save(defectType7);
        DefectType defectType8 = new DefectType();
        defectType8.setName("ខ្ពស់ ទាប/ High/Low");
        defectTypeRepository.save(defectType8);
        DefectType defectType9 = new DefectType();
        defectType9.setName("រលក/ Wavy");
        defectTypeRepository.save(defectType9);
        DefectType defectType10 = new DefectType();
        defectType10.setName("ថ្នេរតឹង/ Tight Tension");
        defectTypeRepository.save(defectType10);
        DefectType defectType11 = new DefectType();
        defectType11.setName("អត់ជាប់/ Unsecured");
        defectTypeRepository.save(defectType11);
        DefectType defectType12 = new DefectType();
        defectType12.setName("ខុសសេរី/ Wrong Size Label");
        defectTypeRepository.save(defectType12);
        DefectType defectType13 = new DefectType();
        defectType13.setName("ដេរផ្លាកវៀច/ Att. label slanted");
        defectTypeRepository.save(defectType13);
        DefectType defectType14 = new DefectType();
        defectType14.setName("សល់ជាយក្រណាត់/ Raw Edge Bottom Hem");
        defectTypeRepository.save(defectType14);
        DefectType defectType15 = new DefectType();
        defectType15.setName("ធ្លុះ/ Hole Damage");
        defectTypeRepository.save(defectType15);
        DefectType defectType16 = new DefectType();
        defectType16.setName("ប្រឡាក់ដី ប្រេង/ Dirt Stain / Oil Stain");
        defectTypeRepository.save(defectType16);
        DefectType defectType17 = new DefectType();
        defectType17.setName("ខុសពណ៌/ Off Shade");
        defectTypeRepository.save(defectType17);
        DefectType defectType18 = new DefectType();
        defectType18.setName("អត់កាត់អំបោះ/ Untrimmnd Thread");
        defectTypeRepository.save(defectType18);
        DefectType defectType19 = new DefectType();
        defectType19.setName("កំហុសផ្សេងៗ/ Other");
        defectTypeRepository.save(defectType19);
    }

    private void initTimeData() {
        for (int i = 7; i < 18; i++) {
            if (i == 11) continue;

            Time time = new Time();
            String from = String.format("%02d:00", i);
            String to = String.format("%02d:00", i + 1);

            time.setName(from + "-" + to);
            timeRepository.save(time);
        }
    }

    private void initBuyer(){
        Buyer nike = new Buyer();
        nike.setName("Nike");
        buyerRepository.save(nike);
    }

    private void initDepartment(){
        Department cutting = new Department();
        cutting.setDepartment("Cutting");
        cutting.setProcessNo(1);
        cutting.setStatus(DepartmentStatus.Inactive);
        departmentRepository.save(cutting);
        Department sewing = new Department();
        sewing.setDepartment("Sewing");
        sewing.setProcessNo(2);
        sewing.setStatus(DepartmentStatus.Inactive);
        departmentRepository.save(sewing);
        Department ironing = new Department();
        ironing.setDepartment("Ironing");
        ironing.setProcessNo(3);
        ironing.setStatus(DepartmentStatus.Inactive);
        departmentRepository.save(ironing);
        Department qc = new Department();
        qc.setDepartment("QC");
        qc.setProcessNo(4);
        qc.setStatus(DepartmentStatus.Inactive);
        departmentRepository.save(qc);
        Department packing = new Department();
        packing.setDepartment("Packing");
        packing.setProcessNo(5);
        packing.setStatus(DepartmentStatus.Inactive);
        departmentRepository.save(packing);
    }

    private void initProductionLine(){
        ProductionLine cutting = new ProductionLine();
        cutting.setLine("Cutting-L");
        cutting.setStatus(ProductionLineStatus.inactive);
        cutting.setDepartment(departmentRepository.findById(1L).orElseThrow());
        productionLineRepository.save(cutting);
        ProductionLine sewingL1 = new ProductionLine();
        sewingL1.setLine("Sewing-L1");
        sewingL1.setStatus(ProductionLineStatus.inactive);
        sewingL1.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL1);
        ProductionLine sewingL2 = new ProductionLine();
        sewingL2.setLine("Sewing-L2");
        sewingL2.setStatus(ProductionLineStatus.inactive);
        sewingL2.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL2);
        ProductionLine sewingL3 = new ProductionLine();
        sewingL3.setLine("Sewing-L3");
        sewingL3.setStatus(ProductionLineStatus.inactive);
        sewingL3.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL3);
        ProductionLine sewingL4 = new ProductionLine();
        sewingL4.setLine("Sewing-L4");
        sewingL4.setStatus(ProductionLineStatus.inactive);
        sewingL4.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL4);
        ProductionLine sewingL5 = new ProductionLine();
        sewingL5.setLine("Sewing-L5");
        sewingL5.setStatus(ProductionLineStatus.inactive);
        sewingL5.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL5);
        ProductionLine sewingL6 = new ProductionLine();
        sewingL6.setLine("Sewing-L6");
        sewingL6.setStatus(ProductionLineStatus.inactive);
        sewingL6.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL6);
        ProductionLine sewingL7 = new ProductionLine();
        sewingL7.setLine("Sewing-L7");
        sewingL7.setStatus(ProductionLineStatus.inactive);
        sewingL7.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL7);
        ProductionLine sewingL8 = new ProductionLine();
        sewingL8.setLine("Sewing-L8");
        sewingL8.setStatus(ProductionLineStatus.inactive);
        sewingL8.setDepartment(departmentRepository.findById(2L).orElseThrow());
        productionLineRepository.save(sewingL8);
        ProductionLine ironing = new ProductionLine();
        ironing.setLine("Ironing");
        ironing.setStatus(ProductionLineStatus.inactive);
        ironing.setDepartment(departmentRepository.findById(3L).orElseThrow());
        productionLineRepository.save(ironing);
        ProductionLine qc = new ProductionLine();
        qc.setLine("QC");
        qc.setStatus(ProductionLineStatus.inactive);
        qc.setDepartment(departmentRepository.findById(4L).orElseThrow());
        productionLineRepository.save(qc);
        ProductionLine packing = new ProductionLine();
        packing.setLine("Packing");
        packing.setStatus(ProductionLineStatus.inactive);
        packing.setDepartment(departmentRepository.findById(5L).orElseThrow());
        productionLineRepository.save(packing);
    }

    private void initColor(){
        Color black = new Color();
        black.setColor("Black");
        colorRepository.save(black);
        Color white = new Color();
        white.setColor("White");
        colorRepository.save(white);
        Color blue = new Color();
        blue.setColor("Blue");
        colorRepository.save(blue);
        Color pink = new Color();
        pink.setColor("Pink");
        colorRepository.save(pink);
    }

    private void initSize(){
        Size l = new Size();
        l.setSize("L");
        sizeRepository.save(l);
        Size m = new Size();
        m.setSize("M");
        sizeRepository.save(m);
        Size s = new Size();
        s.setSize("S");
        sizeRepository.save(s);
        Size xl = new Size();
        xl.setSize("XL");
        sizeRepository.save(xl);
        Size xxl = new Size();
        xxl.setSize("XXL");
        sizeRepository.save(xxl);
    }

    private void initRole() {
        Map<String, String> roles = Map.of(
                "ADMIN",        "Full system access with user and configuration management privileges",
                "VIEWER",       "Read-only access to display screens and dashboards (TV display role)",
                "MANAGER", "Head of a Department (Sewing/Cutting/etc)",
                "WORKER",       "Handles day-to-day production floor operations and task execution",
                "WAREHOUSE", "Manages inventory, incoming materials, and outgoing finished goods in the warehouse"
        );

        roles.forEach((name, description) -> {
            Role role = new Role();
            role.setName(name);
            role.setDescription(description);
            role.setCreatedAt(LocalDateTime.now());
            roleRepository.save(role);
        });
    }

    private void initUser(){
        Role roleAdmin = roleRepository.findByName("ADMIN").orElseThrow();
        Role roleTv = roleRepository.findByName("VIEWER").orElseThrow();
        Role roleManager = roleRepository.findByName("MANAGER").orElseThrow();
        User user = new User();
        user.setRoles(List.of(roleManager));
        user.setUuid(UUID.randomUUID().toString());
        user.setNameEn("MR Ricky");
        user.setNameKh("រីកគី");
        user.setGender("M");
        user.setUsername("manager");
        user.setEmployeeId("0001");
        user.setPhoneNumber("0987654321");
        user.setUpdatedAt(LocalDateTime.now());
        user.setCreatedAt(LocalDateTime.now());
        user.setStatus(UserStatus.Inactive);
        user.setPassword(passwordEncoder.encode("123"));
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsEnabled(true);
        userRepository.save(user);
        User user1 = new User();
        user1.setRoles(List.of(roleTv));
        user1.setUuid(UUID.randomUUID().toString());
        user1.setUsername("tv");
        user1.setEmployeeId("0000");
        user1.setGender("M");
        user1.setNameEn("TV");
        user1.setNameKh("ទូរទស្សន៍");
        user1.setPhoneNumber("0987654320");
        user1.setUpdatedAt(LocalDateTime.now());
        user1.setCreatedAt(LocalDateTime.now());
        user1.setPassword(passwordEncoder.encode("123"));
        user1.setStatus(UserStatus.Inactive);
        user1.setIsAccountNonExpired(true);
        user1.setIsCredentialsNonExpired(true);
        user1.setIsAccountNonLocked(true);
        user1.setIsEnabled(true);
        userRepository.save(user1);
        User user2 = new User();
        user2.setUuid(UUID.randomUUID().toString());
        user2.setRoles(List.of(roleAdmin));
        user2.setUsername("admin");
        user2.setEmployeeId("0695");
        user2.setNameEn("Udom");
        user2.setNameKh("ឧត្តម");
        user2.setGender("M");
        user2.setPhoneNumber("0987654322");
        user2.setUpdatedAt(LocalDateTime.now());
        user2.setCreatedAt(LocalDateTime.now());
        user2.setStatus(UserStatus.Inactive);
        user2.setPassword(passwordEncoder.encode("123"));
        user.setIsAccountNonExpired(true);
        user2.setIsCredentialsNonExpired(true);
        user2.setIsAccountNonLocked(true);
        user2.setIsEnabled(true);
        userRepository.save(user2);
    }

    private void initTv(){
        Tv tv = new Tv();
        tv.setName("General");
        tv.setCreatedAt(LocalDateTime.now());
        tvRepository.save(tv);
        Tv tv1 = new Tv();
        tv1.setName("Sawing1");
        tv1.setCreatedAt(LocalDateTime.now());
        tvRepository.save(tv1);
        Tv tv2 = new Tv();
        tv2.setName("Sawing2");
        tv2.setCreatedAt(LocalDateTime.now());
        tvRepository.save(tv2);
    }

}

