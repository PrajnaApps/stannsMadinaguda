//package prajna.app.ui;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.DividerItemDecoration;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//
//import prajna.app.R;
//import prajna.app.repository.model.StudentInfo;
//import prajna.app.ui.adapter.SingleAdapter;
//
//public class SingleSelectionActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private ArrayList<StudentInfo> employees = new ArrayList<>();
//    private SingleAdapter adapter;
////    private AppCompatButton btnGetSelected;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.selectchild_layout);
////        this.btnGetSelected = (AppCompatButton) findViewById(R.id.btnGetSelected);
//        this.recyclerView = (RecyclerView) findViewById(R.id.rv_selectdhild);
//
//        getSupportActionBar().setTitle("Single Selection");
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        adapter = new SingleAdapter(this, employees);
//        recyclerView.setAdapter(adapter);
//
//        createList();
//
////        btnGetSelected.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////                if (adapter.getSelected() != null) {
////                    showToast(adapter.getSelected().getStudentName());
////                } else {
////                    showToast("No Selection");
////                }
////            }
////        });
//    }
//
//    private void createList() {
//        employees = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            StudentInfo employee = new StudentInfo();
//            employee.setName("Employee " + (i + 1));
//            employees.add(employee);
//        }
//        adapter.setEmployees(employees);
//    }
//
//    private void showToast(String msg) {
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
//    }
//}