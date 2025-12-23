import javax.swing.*; //ستيراد كل مكونات Swing (Buttons – Frames – Dialogs)
import javax.swing.table.DefaultTableModel; //Model لتخزين بيانات الـ JTable (صفوف × أعمدة)
import java.awt.*; //Layouts – Colors – Components من AWT
import java.awt.event.*; //(Click – Selection – ActionListener)
import java.sql.PreparedStatement; //منع SQL Injection)
import java.util.List; //List للكورسات والطلاب
import java.util.Map; //Map لتخزين درجات الطلاب

public class Main extends JFrame { // Main بيورث من JFrame لإنشاء نافذة رئيسية للتطبيق
    private JTable studentTable, courseTable, studentCourseTable; // جداول لعرض الطلاب والكورسات والكورسات المسجلة لكل
                                                                  // طالب
    private DefaultTableModel studentModel, courseModel, studentCourseModel; // Models لتخزين بيانات الجداول

    private StudentDAO studentDAO = new StudentDAO(); // Object من DAO للتعامل مع بيانات الطلاب
    private CourseDAO courseDAO = new CourseDAO(); // Object من DAO للتعامل مع بيانات الكورسات
    private StudentCourseDAO scDAO = new StudentCourseDAO(); // Object من DAO للتعامل مع تسجيلات الطلاب للكورسات

    public Main() {
        setTitle("Student Management System"); // عنوان النافذة
        setSize(1050, 700); // حجم النافذة
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // إغلاق البرنامج عند غلق النافذة
        setLocationRelativeTo(null); // توسيط النافذة على الشاشة
        setLayout(new BorderLayout()); // تعيين Layout للنافذة

        // Panels
        JPanel topPanel = new JPanel(new GridLayout(1, 3, 10, 10)); // Panel علوي لتقسيمه إلى 3 أعمدة لعرض الجداول
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // إضافة مسافة داخلية للـ Panel

        // Students Table
        studentModel = new DefaultTableModel(new String[] { "ID", "Name", "Type" }, 0); // Model لتخزين بيانات الطلاب
                                                                                        // (أعمدة: ID, Name, Type)
        studentTable = new JTable(studentModel); // إنشاء JTable لعرض بيانات الطلاب
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // تعيين اختيار صف واحد فقط في الجدول
        JScrollPane studentScroll = new JScrollPane(studentTable); // إضافة JScrollPane لتمكين التمرير في حال زيادة
                                                                   // البيانات
        studentScroll.setBorder(BorderFactory.createTitledBorder("Students"));
        topPanel.add(studentScroll); // إضافة JScrollPane إلى الـ Panel العلوي

        // Courses Table
        courseModel = new DefaultTableModel(new String[] { "ID", "Name", "Type" }, 0); // Model لتخزين بيانات الكورسات
                                                                                       // (أعمدة: ID, Name, Type)
        courseTable = new JTable(courseModel); // إنشاء JTable لعرض بيانات الكورسات
        courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // تعيين اختيار صف واحد فقط في الجدول
        JScrollPane courseScroll = new JScrollPane(courseTable); // إضافة JScrollPane لتمكين التمرير في حال زيادة
                                                                 // البيانات
        courseScroll.setBorder(BorderFactory.createTitledBorder("Courses")); // تعيين عنوان للـ JScrollPane
        topPanel.add(courseScroll);

        // Student Courses Table
        studentCourseModel = new DefaultTableModel(new String[] { "ID", "Name", "Type", "Grade" }, 0); // Model لتخزين
                                                                                                       // بيانات
                                                                                                       // الكورسات
                                                                                                       // المسجلة لكل
                                                                                                       // طالب (أعمدة:
                                                                                                       // ID, Name,
                                                                                                       // Type, Grade)
        studentCourseTable = new JTable(studentCourseModel); // إنشاء JTable لعرض بيانات الكورسات المسجلة لكل طالب
        JScrollPane studentCourseScroll = new JScrollPane(studentCourseTable);// إضافة JScrollPane لتمكين التمرير في حال
                                                                              // زيادة البيانات
        studentCourseScroll.setBorder(BorderFactory.createTitledBorder("Student Courses")); // تعيين عنوان للـ
                                                                                            // JScrollPane
        topPanel.add(studentCourseScroll); // إضافة JScrollPane إلى الـ Panel العلوي

        add(topPanel, BorderLayout.CENTER);// إضافة الـ Panel العلوي إلى النافذة الرئيسية في الوسط

        // Buttons Panel
        JPanel buttonPanel = new JPanel(); // Panel سفلي لاحتواء الأزرار
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // إضافة مسافة داخلية للـ Panel
        buttonPanel.setLayout(new GridLayout(2, 4, 10, 10)); // تعيين Layout للـ Panel (2 صفوف × 4 أعمدة مع مسافات
                                                             // بينية)

        JButton addStudentBtn = new JButton("Add Student"); // أزرار لإضافة، تحديث، حذف الطلاب والكورسات وتعيين الكورسات
                                                            // وتحديد الدرجات
        JButton updateStudentBtn = new JButton("Update Student"); // زر لتحديث بيانات الطالب
        JButton deleteStudentBtn = new JButton("Delete Student"); // زر لحذف الطالب

        JButton addCourseBtn = new JButton("Add Course"); // زر لإضافة كورس جديد
        JButton updateCourseBtn = new JButton("Update Course"); // زر لتحديث بيانات الكورس
        JButton deleteCourseBtn = new JButton("Delete Course"); // زر لحذف الكورس

        JButton assignCourseBtn = new JButton("Assign Course"); // زر لتعيين كورس لطالب
        JButton setGradeBtn = new JButton("Set Grade"); // زر لتحديد درجة لطالب في كورس معين
        buttonPanel.add(setGradeBtn); // إضافة زر تحديد الدرجة إلى الـ Panel السفلي

        buttonPanel.add(addStudentBtn);// إضافة زر إضافة الطالب إلى الـ Panel السفلي
        buttonPanel.add(updateStudentBtn); // إضافة زر تحديث الطالب إلى الـ Panel السفلي
        buttonPanel.add(deleteStudentBtn);// إضافة زر حذف الطالب إلى الـ Panel السفلي
        buttonPanel.add(new JLabel()); // empty

        buttonPanel.add(addCourseBtn);
        buttonPanel.add(updateCourseBtn);
        buttonPanel.add(deleteCourseBtn);
        buttonPanel.add(assignCourseBtn);

        add(buttonPanel, BorderLayout.SOUTH); // إضافة الـ Panel السفلي إلى النافذة الرئيسية في الأسفل

        // Load data initially
        loadStudents(); // تحميل بيانات الطلاب عند بدء التطبيق
        loadCourses(); // تحميل بيانات الكورسات عند بدء التطبيق

        // Listeners
        studentTable.getSelectionModel().addListSelectionListener(e -> { // عند اختيار طالب من جدول الطلاب
            int row = studentTable.getSelectedRow(); // الحصول على الصف المحدد
            if (row >= 0) { // إذا تم اختيار صف
                int studentId = Integer.parseInt(studentModel.getValueAt(row, 0).toString()); // الحصول على ID الطالب من
                                                                                              // الـ Model
                loadStudentCourses(studentId); // تحميل الكورسات المسجلة لهذا الطالب
            }
        });

        // Button Actions
        addStudentBtn.addActionListener(e -> addStudent()); // إضافة مستمع للأزرار لتنفيذ العمليات المناسبة عند النقر
                                                            // عليها
        updateStudentBtn.addActionListener(e -> updateStudent()); // زر لتحديث بيانات الطالب
        deleteStudentBtn.addActionListener(e -> deleteStudent()); // زر لحذف الطالب

        addCourseBtn.addActionListener(e -> addCourse()); // زر لإضافة كورس جديد
        updateCourseBtn.addActionListener(e -> updateCourse()); // زر لتحديث بيانات الكورس
        deleteCourseBtn.addActionListener(e -> deleteCourse()); // زر لحذف الكورس

        assignCourseBtn.addActionListener(e -> assignCourse()); // زر لتعيين كورس لطالب
        setGradeBtn.addActionListener(e -> { // زر لتحديد درجة لطالب في كورس معين

            int studentRow = studentTable.getSelectedRow(); // الحصول على الصف المحدد في جدول الطلاب
            int courseRow = studentCourseTable.getSelectedRow(); // الحصول على الصف المحدد في جدول كورسات الطالب

            // ✅ تحقق من اختيار طالب وكورس

            if (studentRow < 0 || courseRow < 0) { // إذا لم يتم اختيار طالب أو كورس
                JOptionPane.showMessageDialog(this,
                        "Please select a student and one of his courses first"); // عرض رسالة تحذير للمستخدم
                return;
            }

            try {
                int studentId = Integer.parseInt(
                        studentModel.getValueAt(studentRow, 0).toString()); // الحصول على ID الطالب من الـ Model

                int courseId = Integer.parseInt(
                        studentCourseModel.getValueAt(courseRow, 0).toString()); // الحصول على ID الكورس من الـ Model

                // ✅ تأكد إن الطالب مسجل للكورس
                if (!scDAO.isStudentAssignedToCourse(studentId, courseId)) { // التحقق من تسجيل الطالب للكورس باستخدام
                                                                             // DAO
                    JOptionPane.showMessageDialog(this,
                            "This student is not registered in this course!");
                    return;
                }

                String gradeInput = JOptionPane.showInputDialog( // طلب إدخال الدرجة من المستخدم
                        this, "Enter grade (0 - 100):");

                if (gradeInput == null || gradeInput.trim().isEmpty()) // إذا لم يتم إدخال قيمة
                    return;

                double grade = Double.parseDouble(gradeInput); // تحويل القيمة المدخلة إلى رقم عشري

                // ✅ استخدم Singleton الخاص بالـ Grades
                grade = GradeProcessing
                        .getInstance()
                        .normalize(grade);

                gradeDAO.setGrade(studentId, courseId, grade); // تعيين الدرجة في الـ DAO

                loadStudentCourses(studentId); // إعادة تحميل الكورسات المسجلة للطالب لتحديث العرض

                JOptionPane.showMessageDialog(this, "Grade saved successfully ✔"); // إعلام المستخدم بنجاح العملية

            } catch (NumberFormatException ex) { // التقاط استثناء في حالة إدخال قيمة غير صالحة
                JOptionPane.showMessageDialog(this,
                        "Invalid grade value!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Error while saving grade");
            }
        });

        // Improve aesthetics
        UIManager.put("Table.gridColor", Color.LIGHT_GRAY); // تغيير لون شبكة الجداول
        UIManager.put("Table.selectionBackground", new Color(135, 206, 250)); // تغيير لون خلفية الصف المحدد في الجداول
        UIManager.put("Table.selectionForeground", Color.BLACK); // تغيير لون نص الصف المحدد في الجداول

        setVisible(true);
    }

    // =================== Methods ====================

    private void loadStudents() { // تحميل بيانات الطلاب من قاعدة البيانات وعرضها في الجدول
        try {
            studentModel.setRowCount(0);
            List<Student> students = studentDAO.getAll(); // جلب كل الطلاب من قاعدة البيانات باستخدام DAO
            for (Student s : students) {
                studentModel.addRow(new Object[] { s.getId(), s.getName(), s.getType() });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // التقاط أي استثناء وطباعة تفاصيله
    }

    private void loadCourses() { // تحميل بيانات الكورسات من قاعدة البيانات وعرضها في الجدول
        try {
            courseModel.setRowCount(0); // مسح البيانات الحالية في الـ Model
            List<Course> courses = courseDAO.getAll();
            for (Course c : courses) {
                courseModel.addRow(new Object[] { c.getId(), c.getName(), c.getType() }); // إضافة صف جديد لكل كورس في
                                                                                          // الـ Model
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private StudentCourseGradeDAO gradeDAO = new StudentCourseGradeDAO(); // Object من DAO للتعامل مع درجات الطلاب في
                                                                          // الكورسات

    private void loadStudentCourses(int studentId) {
        try {
            studentCourseModel.setRowCount(0); // مسح البيانات الحالية في الـ Model
            List<Course> courses = scDAO.getCoursesForStudent(studentId);
            Map<Integer, Double> grades = gradeDAO.getGradesForStudent(studentId); // جلب درجات الطالب في الكورسات
                                                                                   // المسجلة له
            for (Course c : courses) {
                double grade = grades.getOrDefault(c.getId(), 0.0);
                studentCourseModel.addRow(new Object[] { c.getId(), c.getName(), c.getType(), grade }); // إضافة صف جديد
                                                                                                        // لكل كورس مع
                                                                                                        // الدرجة في الـ
                                                                                                        // Model
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // التقاط أي استثناء وطباعة تفاصيله
    }

    private void addStudent() { // إضافة طالب جديد
        try {
            String name = JOptionPane.showInputDialog(this, "Enter Student Name:"); // طلب إدخال اسم الطالب من المستخدم
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Student name required", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] types = { "Undergraduate", "Graduate", "PartTime" };
            String type = (String) JOptionPane.showInputDialog(this, "Select Type", "Student Type",
                    JOptionPane.PLAIN_MESSAGE, null, types, types[0]); // طلب اختيار نوع الطالب من المستخدم
            if (type == null)
                return;

            Student s = StudentFactory.createStudent(type, name.trim()); // إنشاء كائن طالب جديد باستخدام Factory بناءً
                                                                         // على النوع المختار
            studentDAO.insert(s);// إدخال الطالب الجديد في قاعدة البيانات باستخدام DAO
            loadStudents();// إعادة تحميل بيانات الطلاب لتحديث العرض
        } catch (Exception ex) {
            ex.printStackTrace();
        } // التقاط أي استثناء وطباعة تفاصيله
    }

    private void updateStudent() { // تحديث بيانات طالب موجود
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select student first");
            return;
        } // التحقق من اختيار طالب من الجدول
        // إذا لم يتم اختيار طالب، عرض رسالة تحذير والخروج من الدالة
        try {
            int id = Integer.parseInt(studentModel.getValueAt(row, 0).toString()); // الحصول على ID الطالب من الـ Model
            String currentName = studentModel.getValueAt(row, 1).toString();
            String name = JOptionPane.showInputDialog(this, "Edit Name:", currentName);// طلب إدخال الاسم الجديد من
                                                                                       // المستخدم مع عرض الاسم الحالي
                                                                                       // كقيمة افتراضية
            if (name == null || name.trim().isEmpty())
                return;
            // For simplicity, we update name only
            Student s = StudentFactory.createStudent(studentModel.getValueAt(row, 2).toString(), name.trim()); // إنشاء
                                                                                                               // كائن
                                                                                                               // طالب
                                                                                                               // جديد
                                                                                                               // باستخدام
                                                                                                               // Factory
                                                                                                               // بناءً
                                                                                                               // على
                                                                                                               // النوع
                                                                                                               // الحالي
            s.setId(id);
            PreparedStatement ps = DBConnection.getInstance().getConnection()
                    .prepareStatement("UPDATE Student SET Name=? WHERE Id=?");
            ps.setString(1, s.getName());
            ps.setInt(2, s.getId());
            ps.executeUpdate();
            loadStudents();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteStudent() { // حذف طالب موجود
        int row = studentTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select student first");
            return;
        } // التحقق من اختيار طالب من الجدول
        // إذا لم يتم اختيار طالب، عرض رسالة تحذير والخروج من الدالة
        try {
            int id = Integer.parseInt(studentModel.getValueAt(row, 0).toString()); // الحصول على ID الطالب من الـ Model
            PreparedStatement ps = DBConnection.getInstance().getConnection() // الحصول على اتصال بقاعدة البيانات
                    .prepareStatement("DELETE FROM Student WHERE Id=?"); // إعداد استعلام SQL لحذف الطالب بناءً على الـ
                                                                         // ID
            ps.setInt(1, id);
            ps.executeUpdate(); // تنفيذ الاستعلام لحذف الطالب من قاعدة البيانات
            loadStudents();
            studentCourseModel.setRowCount(0); // مسح بيانات كورسات الطالب المعروض في الجدول
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void addCourse() {
        try {
            String name = JOptionPane.showInputDialog(this, "Enter Course Name:");
            if (name == null || name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Course name required", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            String[] types = { "Core", "Elective", "Lab" };
            String type = (String) JOptionPane.showInputDialog(this, "Select Type", "Course Type",
                    JOptionPane.PLAIN_MESSAGE, null, types, types[0]);// طلب اختيار نوع الكورس من المستخدم
            if (type == null)
                return;

            Course c = CourseFactory.createCourse(type, name.trim());// إنشاء كائن كورس جديد باستخدام Factory بناءً على
                                                                     // النوع المختار
            courseDAO.insert(c);
            loadCourses();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void updateCourse() {
        int row = courseTable.getSelectedRow(); // الحصول على الصف المحدد في جدول الكورسات
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select course first");
            return;
        } // التحقق من اختيار كورس من الجدول
        // إذا لم يتم اختيار كورس، عرض رسالة تحذير والخروج من الدالة
        try {
            int id = Integer.parseInt(courseModel.getValueAt(row, 0).toString()); // الحصول على ID الكورس من الـ Model
            String currentName = courseModel.getValueAt(row, 1).toString();
            String name = JOptionPane.showInputDialog(this, "Edit Name:", currentName);
            if (name == null || name.trim().isEmpty())
                return;
            Course c = CourseFactory.createCourse(courseModel.getValueAt(row, 2).toString(), name.trim()); // إنشاء كائن
                                                                                                           // كورس جديد
                                                                                                           // باستخدام
                                                                                                           // Factory
                                                                                                           // بناءً على
                                                                                                           // النوع
                                                                                                           // الحالي
            c.setId(id);
            PreparedStatement ps = DBConnection.getInstance().getConnection() // الحصول على اتصال بقاعدة البيانات
                    .prepareStatement("UPDATE Course SET Name=? WHERE Id=?"); // إعداد استعلام SQL لتحديث اسم الكورس
                                                                              // بناءً على الـ ID
            ps.setString(1, c.getName());
            ps.setInt(2, c.getId());
            ps.executeUpdate();// تنفيذ الاستعلام لتحديث بيانات الكورس في قاعدة البيانات
            loadCourses();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void deleteCourse() { // حذف كورس موجود
        int row = courseTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select course first");
            return;
        } // التحقق من اختيار كورس من الجدول
        // إذا لم يتم اختيار كورس، عرض رسالة تحذير والخروج من الدالة
        try {
            int id = Integer.parseInt(courseModel.getValueAt(row, 0).toString());// الحصول على ID الكورس من الـ Model
            PreparedStatement ps = DBConnection.getInstance().getConnection() // الحصول على اتصال بقاعدة البيانات
                    .prepareStatement("DELETE FROM Course WHERE Id=?"); // إعداد استعلام SQL لحذف الكورس بناءً على الـ
                                                                        // ID
            ps.setInt(1, id);
            ps.executeUpdate();
            loadCourses();
        } catch (Exception ex) {
            ex.printStackTrace();
        } // التقاط أي استثناء وطباعة تفاصيله
    }

    private void assignCourse() {
        int sRow = studentTable.getSelectedRow(); // الحصول على الصف المحدد في جدول الطلاب
        int cRow = courseTable.getSelectedRow();
        // ✅ تحقق من اختيار طالب وكورس
        if (sRow < 0 || cRow < 0) {
            JOptionPane.showMessageDialog(this, "Select student and course first");
            return;
        }
        try {
            int studentId = Integer.parseInt(studentModel.getValueAt(sRow, 0).toString()); // الحصول على ID الطالب من
                                                                                           // الـ Model
            int courseId = Integer.parseInt(courseModel.getValueAt(cRow, 0).toString()); // الحصول على ID الكورس من الـ
                                                                                         // Model

            // هنا نستخدم Singleton
            CourseRegistration crs = CourseRegistration.getInstance(); // الحصول على الـ Instance الوحيد من Singleton
            Student student = StudentFactory.createStudent(studentModel.getValueAt(sRow, 2).toString(),
                    studentModel.getValueAt(sRow, 1).toString());
            student.setId(studentId);
            Course course = CourseFactory.createCourse(courseModel.getValueAt(cRow, 2).toString(),
                    courseModel.getValueAt(cRow, 1).toString());
            course.setId(courseId);

            crs.registerCourse(student, course); // تسجيل الكورس في Singleton
            scDAO.assignCourse(studentId, courseId); // حفظ في الداتا بيز
            loadStudentCourses(studentId); // إعادة تحميل الكورسات المسجلة للطالب
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "This course is already assigned to the student!"); // منع التكرار
        }
    }

    public static void main(String[] args) { // نقطة الدخول للتطبيق
        SwingUtilities.invokeLater(Main::new); // تشغيل الواجهة الرسومية في Thread منفصل
    }
}