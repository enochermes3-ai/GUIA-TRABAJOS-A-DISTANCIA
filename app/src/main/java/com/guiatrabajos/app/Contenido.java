package com.guiatrabajos.app;

/** Textos fijos de la guía: categorías, comparativa, fases, prompts y avisos. */
final class Contenido {
    private Contenido() {}

    static final String[] CATEGORIAS = {
        "A. Reubicación", "B. Ingresos digitales", "C. Microtareas",
        "D. Vacantes remotas", "D. Freelance", "D. Tableros por área", "E. CV con IA"
    };

    static final String[] PERFILES = {
        "General", "Tecnología e IA", "Redacción y contenido", "Soporte al cliente",
        "Marketing y ventas", "Diseño y creativo", "Idiomas", "Reubicación"
    };

    /** categoría, enfoque, ejemplos, tipo de ingreso */
    static final String[][] COMPARATIVA = {
        {"A. Reubicación y subsidios",
         "Países y comunidades que pagan a extranjeros o familias por mudarse y combatir la despoblación.",
         "Anticiteras (Grecia), Albinen (Suiza), Presicce-Acquarica (Italia)",
         "Subsidio o estipendio"},
        {"B. Afiliados e ingresos digitales",
         "Ingresos promocionando productos de marcas globales con tráfico orgánico, y venta de diseños sin inventario.",
         "Programa de afiliados LEGO, Pinterest, Zazzle, Etsy",
         "Comisiones variables"},
        {"C. Microtareas y evaluaciones",
         "Trabajo independiente con bajo umbral de entrada y sin entrevista tradicional.",
         "DataAnnotation, CrowdGen, Telus, Cambly, TranscribeMe, Rev, Respondent, Prolific",
         "Pago por tarea u hora"},
        {"D. Empleo remoto y freelance",
         "Vacantes 100% remotas contratadas en USD o EUR y servicios freelance profesionales.",
         "Vacantes.lat, Remote OK, Torre, LinkedIn, Indeed, Upwork, Booking.com, Wise, Klarna",
         "Salario o tarifa"},
        {"E. Optimización del CV con IA",
         "Herramientas que adaptan el CV a cada oferta para superar filtros automáticos (ATS).",
         "Hiremark, Jobscan, JobStep.io, Kickresume",
         "Herramienta de apoyo"},
        {"F. Prompts de IA para monetizar ideas",
         "Plantillas de ChatGPT para validar una idea, definir clientes, ofertas, alianzas y contenido.",
         "Pestaña Prompts de esta app",
         "Herramienta de apoyo"},
    };

    /** título de la fase, pasos... */
    static final String[][] FASES = {
        {"Fase 1: Preparación del perfil y adaptación tecnológica",
         "Obtén certificaciones gratuitas con alta demanda en Google Skillshop (Google Ads, Analytics) o HubSpot Academy.",
         "Optimiza tu CV con Hiremark, Jobscan, JobStep.io o Kickresume para asegurar compatibilidad con sistemas ATS; adapta tu CV a cada oferta en lugar de usar el mismo para todas.",
         "Prepara perfiles profesionales verificados en Upwork, LinkedIn y Torre.ai."},
        {"Fase 2: Plataformas de entrada rápida",
         "Inscríbete en DataAnnotation.tech y realiza la prueba de evaluación con alta atención al detalle.",
         "Aplica en Telus International o CrowdGen para proyectos de evaluación de datos.",
         "Si deseas diversificar, explora PlaylistPush o Airfriend.me."},
        {"Fase 3: Búsqueda de empleo remoto profesional",
         "Monitorea diariamente Vacantes.lat para postular en empresas estadounidenses con contratación remota en LatAm.",
         "Antes de postularte en Booking.com, Wise, Klarna, Hostinger o SIXT, pasa el enlace de la oferta por JobStep.io o Hiremark para personalizar las palabras clave de tu CV.",
         "Complementa la búsqueda con los tableros por especialidad (We Work Remotely, Remotive, Arc, Support Driven, ProBlogger)."},
        {"Fase 4: Ingresos digitales complementarios",
         "Solicita tu ingreso en redes de afiliados (Impact, Rakuten, Amazon Associates, LEGO Affiliate Program).",
         "Crea una cuenta Business en Pinterest y diseña tableros temáticos de alta calidad con pines visuales atractivos.",
         "Incluye siempre la declaración explícita de enlace de afiliado (#ad / #afiliado) conforme a la regulación legal.",
         "Abre perfiles en Zazzle o Etsy para vender diseños bajo demanda.",
         "Configura tu perfil en Upwork ofreciendo asistencia virtual o gestión de campañas de Google Ads.",
         "Usa los prompts de la pestaña Prompts para validar tu idea, definir tu oferta y planear 30 días de contenido antes de invertir dinero."},
        {"Fase 5: Evaluación de programas de relocalización internacional",
         "Investiga directamente en las páginas oficiales de los municipios (por ejemplo, la Comuna de Albinen o el Comune di Presicce-Acquarica).",
         "Revisa los requisitos de visado de trabajo o residencia y tu capacidad financiera inicial antes de tomar decisiones migratorias."},
    };

    /** título, qué hace, texto del prompt (con [corchetes] para personalizar) */
    static final String[][] PROMPTS = {
        {"1. El perfilador psicológico",
         "Perfil del cliente ideal: qué desea, qué le frustra, dónde pasa el tiempo online y qué lo lleva a comprar.",
         "Actúa como especialista en psicología del consumidor. Describe a fondo al cliente ideal de [tu idea o negocio]: qué quiere lograr, qué le frustra, en qué sitios pasa su tiempo en internet y qué lo empuja a decidir una compra. Al final, escribe 5 frases con las que podría captar su atención."},
        {"2. El destructor de objeciones",
         "Objeciones probables del cliente, cada una con tres respuestas (con pruebas, con lógica y emocional), en formato FAQ.",
         "Enumera las objeciones que tendría un cliente antes de comprar [tu producto o servicio]. Para cada una, escribe tres respuestas: una apoyada en pruebas, otra en lógica y otra que apele a la emoción. Presenta todo como una lista de preguntas frecuentes."},
        {"3. El inversionista exigente",
         "Evalúa tu idea como lo haría un inversionista: problema, quién pagaría, competencia y cómo probarla rápido y barato.",
         "Evalúa [tu idea] como lo haría un inversionista exigente. Indica qué problema resuelve, quién pagaría por ella, qué competencia existe y cuál sería la forma más rápida y barata de probarla esta semana."},
        {"4. El arquitecto de ofertas",
         "Convierte la idea en una oferta sólida: qué incluye, cómo presentarla, tres precios posibles y una garantía.",
         "Transforma [tu idea] en una oferta difícil de rechazar. Define qué incluye, cómo presentarla, propón tres precios posibles y una garantía que reduzca el riesgo para el cliente."},
        {"5. El hacker de alianzas",
         "10 oportunidades de colaboración en tu nicho: a quién contactar, cómo acercarte y qué ganarían ambas partes.",
         "Encuentra 10 oportunidades de colaboración para [tu nicho]. Para cada una, indica a quién contactar, cómo acercarme y qué beneficio obtendríamos ambas partes."},
        {"6. El motor de contenido",
         "Calendario de 30 días: ideas de publicaciones, gancho de cada una y llamada a la acción sencilla.",
         "Crea un calendario de contenido de 30 días para atraer clientes a [tu negocio]. Incluye la idea de cada publicación, el gancho con el que abriría y una llamada a la acción sencilla."},
    };

    static final String AVISO_REQUISITOS =
        "Los programas de reubicación no son dinero libre de condiciones. Requieren residencia legal (visado de residencia o ciudadanía UE/Suiza), compromiso de permanencia mínima (habitualmente 10 años) y compra o restauración efectiva de inmuebles. En Albinen, además, se exige un límite de edad (menores de 45 años) y una inversión mínima en la propiedad.";
    static final String AVISO_AFILIADOS =
        "Prometer $8,000 USD al mes de forma garantizada es engañoso. El marketing de afiliados efectivo requiere divulgar el enlace (normas FTC/GDPR), crear contenido original (evitar spam en Pinterest) y construir tráfico recurrente. Las comisiones de LEGO suelen oscilar entre 3% y 10% por venta.";
    static final String AVISO_CIFRAS =
        "Las cifras publicitadas varían según proyecto, país y disponibilidad de tareas. Verifica las condiciones en el sitio oficial de cada plataforma antes de compartir datos personales o bancarios.";
    static final String AVISO_TABLEROS =
        "Los nombres y direcciones de los tableros por área provienen de una publicación en redes sociales. Confirma que cada sitio sea legítimo y nunca pagues por \"acceso garantizado\" a una vacante. Algunos portales (FlexJobs, Toptal) tienen requisitos o costos propios.";
    static final String AVISO_PROMPTS =
        "Los prompts son una ayuda para ordenar ideas, no una garantía de ingresos. Contrasta las respuestas de la IA con datos reales del mercado: conversaciones con clientes, competidores y pruebas pequeñas.";
    static final String LEYENDA =
        "— = dato no especificado en las fuentes. Los pagos son cifras anunciadas en videos y publicaciones; no son garantía de ingreso. Los enlaces llevan al sitio principal de cada plataforma; si una ficha no tiene enlace, el botón abre una búsqueda en Google.";
}
