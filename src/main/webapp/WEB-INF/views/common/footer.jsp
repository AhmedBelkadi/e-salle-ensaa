        </main>
        
        <!-- Footer -->
        <footer class="bg-white border-t border-gray-200 px-6 py-4 mt-auto">
            <div class="flex flex-col md:flex-row justify-between items-center text-sm text-gray-600">
                <div class="mb-2 md:mb-0">
                    <p>&copy; 2025 E-Salle ENSAA. Tous droits réservés.</p>
                </div>
                <div>
                    <p>Développé avec <span class="text-red-500">❤️</span> pour l'ENSAA</p>
                </div>
            </div>
        </footer>
    </div>

    <script>
        // Toggle sidebar functionality
        document.addEventListener('DOMContentLoaded', function() {
            const sidebar = document.getElementById('sidebar');
            const toggleButton = document.getElementById('toggleSidebar');
            const sidebarHeader = document.getElementById('sidebarHeader');
            const navTexts = document.querySelectorAll('.nav-text');
            const userInfo = document.querySelector('.user-info');
            const mainContent = document.querySelector('.ml-64');
            
            if (toggleButton) {
                toggleButton.addEventListener('click', function() {
                    if (sidebar.classList.contains('w-64')) {
                        // Minimize sidebar
                        sidebar.classList.remove('w-64');
                        sidebar.classList.add('w-16');
                        if (mainContent) {
                            mainContent.classList.remove('ml-64');
                            mainContent.classList.add('ml-16');
                        }
                        toggleButton.innerHTML = '<i class="fas fa-chevron-right w-4 h-4"></i>';
                        
                        // Hide text elements
                        if (sidebarHeader) sidebarHeader.style.display = 'none';
                        navTexts.forEach(text => text.style.display = 'none');
                        if (userInfo) userInfo.style.display = 'none';
                    } else {
                        // Expand sidebar
                        sidebar.classList.remove('w-16');
                        sidebar.classList.add('w-64');
                        if (mainContent) {
                            mainContent.classList.remove('ml-16');
                            mainContent.classList.add('ml-64');
                        }
                        toggleButton.innerHTML = '<i class="fas fa-chevron-left w-4 h-4"></i>';
                        
                        // Show text elements
                        if (sidebarHeader) sidebarHeader.style.display = 'block';
                        navTexts.forEach(text => text.style.display = 'inline');
                        if (userInfo) userInfo.style.display = 'block';
                    }
                });
            }
            
            // Highlight active page
            const currentPath = window.location.pathname;
            const navLinks = document.querySelectorAll('nav a');
            
            navLinks.forEach(link => {
                const button = link.querySelector('button');
                const href = link.getAttribute('href');
                
                // Remove query string and hash for comparison
                const cleanPath = currentPath.split('?')[0].split('#')[0];
                const cleanHref = href ? href.split('?')[0].split('#')[0] : '';
                
                if (cleanPath === cleanHref || (cleanHref !== '' && cleanPath.startsWith(cleanHref))) {
                    // Add active state
                    button.classList.remove('text-gray-700', 'hover:bg-gray-100');
                    button.classList.add('bg-blue-600', 'hover:bg-blue-700', 'text-white');
                }
            });
        });
    </script>
</body>
</html>
